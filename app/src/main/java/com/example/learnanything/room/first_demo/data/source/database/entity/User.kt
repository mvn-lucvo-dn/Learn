package com.example.learnanything.room.first_demo.data.source.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
@Entity(tableName = "user")
data class User(val name: String? = "", val age: Int? = 0) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
