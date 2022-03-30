package com.example.learnanything.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.util.Log.d
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.learnanything.R
import com.example.learnanything.activity.DefaultActivity
import com.example.learnanything.broadcast.AirPlanBroadCast
import kotlin.random.Random

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class FirstService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private var receiver = AirPlanBroadCast()
    private var binder = LocalBinder()

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            d("aaa", "run")
            try {
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                // Restore interrupt status.
                Thread.currentThread().interrupt()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        d("aaa", "onCreate")
        HandlerThread("ServiceStartArguments").apply {
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
        registerReceiver(receiver, IntentFilter("com.example.broadcast.MY_NOTIFICATION"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        d("aaa", "onStartCommand")
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }
        smallNotification()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        d("aaa", "onBind")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        d("aaa", "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        d("aaa", "onDestroy")
        serviceLooper = null
        serviceHandler?.removeCallbacksAndMessages(null)
        unregisterReceiver(receiver)
    }

    internal fun getRandomNumber(): Int {
        return Random.nextInt(100)
    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun smallNotification() {
        val notificationIntent = Intent(this, DefaultActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
//        val pendingIntent =
//            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent("com.example.broadcast.MY_NOTIFICATION"),
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("demoNotification")
            .setContentText("this is first time notification")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent non venenatis lorem, ac accumsan tortor. Suspendisse hendrerit lacus sed justo tempus fringilla et eget quam. Sed mollis tempor justo vitae feugiat. Vestibulum dictum, dolor ut auctor luctus, nibh quam consectetur augue, non hendrerit diam ante in est. Vestibulum at mi lectus. Nam non nisi rhoncus, dignissim ligula sed, consequat est. Phasellus sem nisl, bibendum in erat eget, sollicitudin efficitur sapien. Proin pretium iaculis feugiat."
                )
            )
            .setNumber(10)
            .setContentIntent(pendingIntent) // action when click notification
            .setAutoCancel(true)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "CHANNEL_NAME",
                NotificationManager.IMPORTANCE_HIGH // heads - up
            )
            notificationManager.createNotificationChannel(channel)
        }
        startForeground(1, builder.build())
    }

    inner class LocalBinder : Binder() {

        fun getService() = this@FirstService
    }
}