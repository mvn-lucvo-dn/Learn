package com.example.learnanything.activity

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.broadcast.AirPlanBroadCast
import com.example.learnanything.databinding.ActivityDefaultBinding
import com.example.learnanything.service.ServiceActivity

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class DefaultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDefaultBinding
    private var receiver = AirPlanBroadCast()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        d("aaa", "$isTaskRoot")
        binding = ActivityDefaultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnShow.setOnClickListener {
            val intent = Intent(this, ServiceActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        d("aaa", "onNewIntent")
    }

    override fun onResume() {
        super.onResume()
        d("aaa", "default resume")
    }

    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(receiver)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
