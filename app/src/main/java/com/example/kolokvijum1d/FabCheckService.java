package com.example.kolokvijum1d;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.Manifest;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class FabCheckService extends Service {

    public static final String CHANNEL_ID = "fab_channel";
    public static final String ACTION_FAB_RED = "com.example.kolokvijum1d.FAB_RED";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ZadatakCountReceiver.fabIsRed) {
            sendNotification();
            sendUiBroadcast();
        }
        stopSelf();
        return START_NOT_STICKY;
    }

    private void sendNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        NotificationCompat.Builder b = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("FAB")
                .setContentText("Crveno je!");

        NotificationManagerCompat.from(this).notify(3, b.build());
    }

    private void sendUiBroadcast() {
        Intent i = new Intent(ACTION_FAB_RED);
        i.setPackage(getPackageName());
        sendBroadcast(i);
    }

    @Nullable @Override
    public IBinder onBind(Intent intent) { return null; }
}