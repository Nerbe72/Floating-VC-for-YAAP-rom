package com.justforme.volumecontroller

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class HeadphoneConnectReceiver(vol_show: TextView) : BroadcastReceiver() {
    var vols = vol_show

    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == BluetoothDevice.ACTION_ACL_CONNECTED) {
            vols.setTextColor(ContextCompat.getColor(context, R.color.green))
        } else if (intent.action == BluetoothDevice.ACTION_ACL_DISCONNECTED || intent.action.equals("android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED")) {
            vols.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
    }
}