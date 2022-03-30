package com.example.learnanything.broadcast

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.databinding.ActivityDragBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class BroadCastReceiverActivity : AppCompatActivity() {

    companion object {
        internal const val ACTION_AIR_PLAN = "ACTION_AIR_PLAN"
    }

    private lateinit var binding: ActivityDragBinding
    private var broadcastReceiver = AirPlanBroadCast()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDragBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /**
         * defined IntentFilter with action to receive message from BR,
         * if not ==> no receive
         */
        registerReceiver(broadcastReceiver, IntentFilter(ACTION_AIR_PLAN))
    }

    /**
     * Method to send broadcast to all receivers
     */
    private fun sendBroadcast() {
        Intent().also { intent ->
            intent.action = ACTION_AIR_PLAN
            sendBroadcast(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}