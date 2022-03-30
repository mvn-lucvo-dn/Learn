package com.example.learnanything

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.activity.DefaultActivity
import com.example.learnanything.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.contentText.setOnClickListener {
            startActivity(Intent(this, DefaultActivity::class.java))
        }
    }
}