package com.example.kolokvijum1d;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ZadaciFragment extends Fragment {

    public static final String ACTION_ZADATAK_ADDED = "com.example.kolokvijum1d.ZADATAK_ADDED";

    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private ZadatakAdapter adapter;
    private ZadatakCountReceiver countReceiver;

    // MODEL unutra
    static class Zadatak {
        String name, time;
        Zadatak(String name, String time) { this.name = name; this.time = time; }
    }

    // ADAPTER unutra — bez item_zadatak.xml
    static class ZadatakAdapter extends RecyclerView.Adapter<ZadatakAdapter.Holder> {
        private final List<Zadatak> list = new ArrayList<>();

        @NonNull @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout row = new LinearLayout(parent.getContext());
            row.setOrientation(LinearLayout.VERTICAL);
            row.setPadding(24, 16, 24, 16);
            TextView tv = new TextView(parent.getContext());
            tv.setTextSize(16);
            row.addView(tv);
            return new Holder(row, tv);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder h, int pos) {
            Zadatak z = list.get(pos);
            h.tv.setText(z.name + " — " + z.time);
        }

        @Override
        public int getItemCount() { return list.size(); }

        void add(Zadatak z) { list.add(z); notifyItemInserted(list.size() - 1); }

        static class Holder extends RecyclerView.ViewHolder {
            TextView tv;
            Holder(LinearLayout row, TextView tv) { super(row); this.tv = tv; }
        }
    }

    public ZadaciFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zadaci, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewZadaci);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ZadatakAdapter();
        recyclerView.setAdapter(adapter);

        fabAdd = view.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showAddDialog());

        // ZAD 9 — receiver
        countReceiver = new ZadatakCountReceiver(fabAdd);
        ContextCompat.registerReceiver(requireContext(), countReceiver,
                new IntentFilter(ACTION_ZADATAK_ADDED), ContextCompat.RECEIVER_NOT_EXPORTED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try { requireContext().unregisterReceiver(countReceiver); } catch (Exception ignored) {}
    }

    // ZAD 7-8 — forma BEZ dialog xml
    private void showAddDialog() {
        var ctx = requireContext();
        LinearLayout layout = new LinearLayout(ctx);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 32, 48, 16);

        EditText etName = new EditText(ctx);
        etName.setHint("Naziv zadatka");
        layout.addView(etName);

        EditText etTime = new EditText(ctx);
        etTime.setHint("Vreme izvršenja");
        etTime.setFocusable(false);
        etTime.setClickable(true);
        final String[] selectedTime = {""};
        etTime.setOnClickListener(v -> new TimePickerDialog(ctx,
                (tp, h, m) -> {
                    selectedTime[0] = String.format("%02d:%02d", h, m);
                    etTime.setText(selectedTime[0]);
                }, 12, 0, true).show());
        layout.addView(etTime);

        LinearLayout btns = new LinearLayout(ctx);
        btns.setOrientation(LinearLayout.HORIZONTAL);
        Button btnOk = new Button(ctx);
        btnOk.setText("Potvrdi");
        Button btnNo = new Button(ctx);
        btnNo.setText("Odustani");
        btns.addView(btnOk, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        btns.addView(btnNo, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        layout.addView(btns);

        AlertDialog dialog = new AlertDialog.Builder(ctx).setView(layout).create();
        btnNo.setOnClickListener(v -> dialog.dismiss());

        btnOk.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty() || selectedTime[0].isEmpty()) return;
            adapter.add(new Zadatak(name, selectedTime[0]));

            Intent i = new Intent(ACTION_ZADATAK_ADDED);
            i.putExtra("count", adapter.getItemCount());
            i.setPackage(ctx.getPackageName());
            ctx.sendBroadcast(i);

            dialog.dismiss();
        });

        dialog.show();
    }
}