package com.example.kolokvijum1d;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class KorisniciFragment extends Fragment {

    private View rootView;
    private BroadcastReceiver yellowReceiver;

    public KorisniciFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_korisnici, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view.findViewById(R.id.korisniciRoot);

        // ZAD 10 — žuta pozadina
        yellowReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (FabCheckService.ACTION_FAB_RED.equals(intent.getAction())) {
                    rootView.setBackgroundColor(Color.YELLOW);
                }
            }
        };
        ContextCompat.registerReceiver(requireContext(), yellowReceiver,
                new IntentFilter(FabCheckService.ACTION_FAB_RED),
                ContextCompat.RECEIVER_NOT_EXPORTED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try { requireContext().unregisterReceiver(yellowReceiver); } catch (Exception ignored) {}
    }
}