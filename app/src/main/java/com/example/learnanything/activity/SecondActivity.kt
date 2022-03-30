package com.example.learnanything.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.HandlerThread
import android.os.Parcelable
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.learnanything.R
import com.example.learnanything.databinding.ActivityDefaultBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDefaultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDefaultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_700))

        binding.btnShow.setOnClickListener {
            smallNotification()
        }
        handleIntent()
    }

    private fun handleIntent() {
        if (intent.action == Intent.ACTION_SEND) {
            (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
                binding.image.setImageURI(it)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        d("aaa", "aaa")
    }

    private fun smallNotification() {
        val notificationIntent = Intent(this, DefaultActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
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
        //Show notification
        notificationManager.notify(1, builder.build())
    }
}