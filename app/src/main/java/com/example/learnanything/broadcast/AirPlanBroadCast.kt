package com.example.learnanything.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log.d
import com.example.learnanything.service.FirstService

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class AirPlanBroadCast : BroadcastReceiver() {

    internal var updateUI: () -> Unit = {}

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == BroadCastReceiverActivity.ACTION_AIR_PLAN){
            d("aaa", "receiver")
        } else {
            d("aaa", "broadcast send")
        }
    }
}