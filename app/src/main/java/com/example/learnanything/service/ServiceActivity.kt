package com.example.learnanything.service

import android.content.*
import android.net.Uri
import android.os.*
import android.provider.UserDictionary
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.broadcast.AirPlanBroadCast
import com.example.learnanything.databinding.ActivityDefaultBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class ServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDefaultBinding
    private var serviceIntent: Intent? = null
    private var service: FirstService? = null
    private var isBound = false
    private var receiver = AirPlanBroadCast()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isBound = true
            val binder = service as FirstService.LocalBinder
            this@ServiceActivity.service = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDefaultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        receiver.updateUI = {
            d("aaa", "updateUI")
        }
        val intentFilter = IntentFilter("com.example.broadcast.MY_NOTIFICATION")
        registerReceiver(receiver, intentFilter)
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//            receiver, intentFilter
//        )

        binding.btnShow.setOnClickListener {
//            sendBroadcast(
//                Intent("com.example.broadcast.MY_NOTIFICATION")
//            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serviceIntent = Intent(this, FirstService::class.java)
                startForegroundService(serviceIntent)
            }
        }
        binding.btnSubmit.setOnClickListener {
            Intent(this, FirstService::class.java).apply {
                bindService(this, serviceConnection, BIND_AUTO_CREATE)
            }
        }

        binding.btnCheck.setOnClickListener {
            d("aaa", "${service?.getRandomNumber()}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceIntent?.let {
            stopService(it)
        }
        if (isBound) {
            unbindService(serviceConnection)
        }
        unregisterReceiver(receiver)
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}
