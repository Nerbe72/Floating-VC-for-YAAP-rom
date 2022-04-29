package com.justforme.volumecontroller

import android.app.*
import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHeadset
import android.content.*
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.NotificationManagerCompat

var isCheck = true

class FloatingVolume : Service() {

    var PENDING_ACTION = "com.example.testsetonclickpendingintent.Pending_Action"

    var params : WindowManager.LayoutParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
        PixelFormat.TRANSLUCENT)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "volume_channel"
            val descriptionText = "설명"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("volume_channel_id", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createPendingIntent(context: Context, reqCode: Int, id: Int, vol_show: TextView?) : PendingIntent {
        var intent = Intent(this, NotificationVolume::class.java).apply {
            action = PENDING_ACTION
            putExtra("id", id)
        }
        intent.action = PENDING_ACTION

        return PendingIntent.getBroadcast(
            this, reqCode, intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreate() {
        createNotificationChannel()
        super.onCreate()

        val windowManager : WindowManager = getSystemService(Service.WINDOW_SERVICE) as WindowManager
        val inflate : LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView : View = inflate.inflate(R.layout.overlay_screen, null)
        val vol_layout : LinearLayout = mView.findViewById(R.id.vol_layout)
        val vol_show : TextView = mView.findViewById(R.id.vol_show)
        val vol_up : View = mView.findViewById(R.id.vol_up)
        val vol_down : View = mView.findViewById(R.id.vol_down)
        val volumeControl : AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val notibar : RemoteViews = RemoteViews(packageName, R.layout.custom_notification)

//        params.x = -(windowManager.defaultDisplay.width/2) + 25
        params.gravity = Gravity.BOTTOM + Gravity.LEFT

        vol_show.setText(volumeControl.getStreamVolume(AudioManager.STREAM_MUSIC).toString())
        windowManager.addView(vol_layout, params)

        //알림
        notibar.setOnClickPendingIntent(R.id.noti_vol_up, createPendingIntent(this, 0, R.id.noti_vol_up, vol_show))
        notibar.setOnClickPendingIntent(R.id.noti_vol_down, createPendingIntent(this, 1, R.id.noti_vol_down, vol_show))

        var builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.volc)
            .setContentTitle("볼륨")
            .setChannelId("volume_channel_id")
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(VISIBILITY_PUBLIC)
            .setContent(notibar)
            .setCustomContentView(notibar)
            .setAutoCancel(false)
            .setOngoing(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1002, builder.build())
        }

        var br : BroadcastReceiver = HeadphoneConnectReceiver(vol_show)
        var fltr: IntentFilter = IntentFilter()
        fltr.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        fltr.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        fltr.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
        registerReceiver(br, fltr)

        vol_up.setOnTouchListener(object: OnSwipeTouchListener(this@FloatingVolume) {
            override fun onSwipeLeft() {}
            override fun onSwipeTop() {}
            override fun onSwipeBottom() {}
            override fun onSwipeRight() {
                volumeControl.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_PLAY_SOUND
                )
                vol_show.setText(volumeControl.getStreamVolume(AudioManager.STREAM_MUSIC).toString())
            }
        })

        vol_down.setOnTouchListener(object: OnSwipeTouchListener(this@FloatingVolume) {
            override fun onSwipeLeft() {}
            override fun onSwipeTop() {}
            override fun onSwipeBottom() {}
            override fun onSwipeRight() {
                volumeControl.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER,
                    AudioManager.FLAG_PLAY_SOUND
                )
                vol_show.setText(volumeControl.getStreamVolume(AudioManager.STREAM_MUSIC).toString())
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        val windowManager : WindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val inflate : LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView : View = inflate.inflate(R.layout.overlay_screen, null)
        val volumeLayout : LinearLayout = mView.findViewById(R.id.vol_layout)
        if (volumeLayout != null) {
            windowManager.removeView(volumeLayout)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
    }
}