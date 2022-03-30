package com.example.learnanything.room.first_demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.R
import com.example.learnanything.room.first_demo.list.UserInfoListFragment
import com.example.learnanything.databinding.ActivityUserInfoBinding
import com.example.learnanything.extension.replaceFragment

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class UserInfoActivity : AppCompatActivity() {

    /**
     * ROOM gồm 3 phần
     *  + Entity: Tương ứng Table trong database
     *  + DAO : Cung cấp các phương thức để truy cập dữ liệu database
     *  + Database : Hold Database
     *  https://developer.android.com/training/data-storage/room
     */

    private lateinit var binding: ActivityUserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(R.id.frPersonContainer, UserInfoListFragment.newInstance())
    }
}