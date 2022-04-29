package com.justforme.volumecontroller

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
//import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.justforme.volumecontroller.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    //    @RequiresApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val powerManager = getSystemService(POWER_SERVICE) as PowerManager
//            if (powerManager.isIgnoringBatteryOptimizations(packageName) == false) {
//                val intent = Intent()
//                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
//                startActivity(intent)
//            }
//        }

        //권한이 없으면 해당 페이지로 연결
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, 0)
            }
        }
        startService(intent)
        startService(Intent(this, FloatingVolume::class.java))


        //바인딩 초기화
        val binding = ActivityMainBinding.inflate(layoutInflater)
//        val overlayBinding = OverlayScreenBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}