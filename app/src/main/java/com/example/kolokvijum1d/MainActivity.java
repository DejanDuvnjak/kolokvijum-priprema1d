package com.example.kolokvijum1d;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private static final int MENU_ZADACI = 1;
    private static final int MENU_KORISNICI = 2;
    private static final long CHECK_INTERVAL = 60_000; // za test stavi 10_000

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createNotificationChannel();
        requestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scheduleFabCheck();
    }

    // ZAD 4-5 — meni BEZ main_menu.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ZADACI, 0, "Zadaci");
        menu.add(0, MENU_KORISNICI, 0, "Korisnici");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        if (item.getItemId() == MENU_ZADACI) {
            tx.replace(R.id.fragment_container, new ZadaciFragment());
            tx.commit();
            return true;
        }
        if (item.getItemId() == MENU_KORISNICI) {
            tx.replace(R.id.fragment_container, new KorisniciFragment());
            tx.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ZAD 10 — AlarmManager svaki minut
    private void scheduleFabCheck() {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, FabCheckService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                CHECK_INTERVAL, pi);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    FabCheckService.CHANNEL_ID, "FAB", NotificationManager.IMPORTANCE_DEFAULT);
            getSystemService(NotificationManager.class).createNotificationChannel(ch);
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }
}