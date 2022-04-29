package com.justforme.volumecontroller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class BootupReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == Intent.ACTION_BOOT_COMPLETED) {
            var rebootIntent = Intent(context, FloatingVolume::class.java);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startService(rebootIntent);
            } else {
                context.startService(rebootIntent);
            }
        }
    }
}