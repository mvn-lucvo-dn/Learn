package com.example.learnanything.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
object PermissionUtil {

    internal const val KEY_PHONE_CALL_PERMISSION = 1245
    internal const val KEY_READ_PHONE_PERMISSION = 1234

    private val PHONE_CALL_ARRAY = arrayOf(
        Manifest.permission.CALL_PHONE
    )

    private val READ_PHONE_CALL_ARRAY = arrayOf(
        Manifest.permission.READ_CONTACTS
    )

    internal fun checkSelfPermission(context: Context, permissionKey: Int) =
        getArrayNameByPermissionKey(permissionKey).any {
            context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }

    internal fun shouldShowRequestPermissionRationale(activity: Activity, permissionKey: Int) =
        getArrayNameByPermissionKey(permissionKey).any {
            activity.shouldShowRequestPermissionRationale(it)
        }

    internal fun getArrayNameByPermissionKey(permissionKey: Int) =
        when (permissionKey) {
            KEY_PHONE_CALL_PERMISSION -> PHONE_CALL_ARRAY
            KEY_READ_PHONE_PERMISSION -> READ_PHONE_CALL_ARRAY
            else -> PHONE_CALL_ARRAY
        }
}