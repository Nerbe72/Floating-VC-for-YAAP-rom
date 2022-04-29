package com.justforme.volumecontroller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.TextView
import android.widget.Toast

class NotificationVolume : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val volumeControl: AudioManager =
            context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        when(intent.getIntExtra("id", 0)){
            R.id.noti_vol_up -> {
                volumeControl.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_PLAY_SOUND
                )
            }
            R.id.noti_vol_down -> {
                volumeControl.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER,
                    AudioManager.FLAG_PLAY_SOUND
                )
            }
            else -> null
        }

    }
}