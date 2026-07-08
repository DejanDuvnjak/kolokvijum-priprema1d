package com.example.kolokvijum1d;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ZadatakCountReceiver extends BroadcastReceiver {

    public static boolean fabIsRed = false;
    private final FloatingActionButton fab;

    public ZadatakCountReceiver(FloatingActionButton fab) {
        this.fab = fab;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ZadaciFragment.ACTION_ZADATAK_ADDED.equals(intent.getAction())) return;
        if (fab == null) return;

        int count = intent.getIntExtra("count", 0);

        if (count % 2 == 1) {
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            fabIsRed = true;
        } else {
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2196F3")));
            fabIsRed = false;
        }
    }
}