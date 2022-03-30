package com.example.learnanything.contentprovider

import android.annotation.SuppressLint
import android.content.ContentProviderOperation
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log.d
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.learnanything.BuildConfig
import com.example.learnanything.databinding.ActivityPhonebookBinding
import com.example.learnanything.util.PermissionUtil
import com.google.android.material.snackbar.Snackbar

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class PhoneBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhonebookBinding
    private lateinit var adapter: PhoneBookAdapter
    private var phoneBooks = mutableListOf<PhoneBookInfo>()
    private val launch = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        d("aaa", "${result.data}")
    }
    private val pref by lazy {
        getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhonebookBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        initViews()
        launch.launch(
            Intent(Intent.ACTION_PICK).apply {
                type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
                flags = FLAG_GRANT_READ_URI_PERMISSION
            }
        )
//        requestPermission(PermissionUtil.KEY_READ_PHONE_PERMISSION) {
//            initDataContacts()
//        }
    }

    private fun initViews() {
        adapter = PhoneBookAdapter(phoneBooks)
        adapter.onMakeCall = this::handlePhoneCall
        binding.recyclerViewPhoneBook.adapter = adapter
    }

    private fun handlePhoneCall() {
        requestPermission(PermissionUtil.KEY_PHONE_CALL_PERMISSION) {
            startActivity(
                Intent(
                    Intent.ACTION_CALL, Uri.parse(
                        "tel: ${adapter.getPhoneNumberSelect()}"
                    )
                )
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtil.KEY_READ_PHONE_PERMISSION) {
            initDataContacts()
        } else if (requestCode == PermissionUtil.KEY_PHONE_CALL_PERMISSION) {
            handlePhoneCall()
        }
    }

    @SuppressLint("Recycle", "Range", "NotifyDataSetChanged")
    private fun initDataContacts() {
        val cursors = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        cursors?.apply {
            /**
             * Moves to the next row in the cursor. Before the first movement in the cursor, the
             * "row pointer" is -1, and if you try to retrieve data at that position you will get an
             * exception.
             */
            while (moveToNext()) {
                val id = getString(getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursors.getString(cursors.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phones = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                    arrayOf(id.toString()),
                    null
                )

                phones?.apply {
                    while (moveToNext()) {
                        val phoneNumber =
                            getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        phoneBooks.add(
                            PhoneBookInfo(name, phoneNumber)
                        )
                    }
                }
                phones?.close()
            }
            close()
        }
        adapter.notifyDataSetChanged()
    }

    private fun requestPermission(permissionKey: Int, onPermissionSuccess: () -> Unit) {
        if (!PermissionUtil.checkSelfPermission(this, permissionKey)) {
            /**
             * shouldShowRequestPermissionRationale return false
             * if the user has never been asked for permission before
             */
            if (PermissionUtil.shouldShowRequestPermissionRationale(
                    this,
                    permissionKey
                )
            ) {
                requestPermissions(
                    PermissionUtil.getArrayNameByPermissionKey(permissionKey),
                    permissionKey
                )
            } else {
                val permissionStr =
                    PermissionUtil.getArrayNameByPermissionKey(permissionKey).first()
                if (isPermissionRequested(permissionStr)) {
                    Snackbar.make(binding.root, "Opp....", 1000).show()
                } else {
                    savePermissionRequest(permissionStr)
                    requestPermissions(
                        PermissionUtil.getArrayNameByPermissionKey(permissionKey),
                        permissionKey
                    )
                }
            }
        } else {
            onPermissionSuccess.invoke()
        }
    }

    private fun isPermissionRequested(permission: String) =
        pref.getBoolean(permission, false)

    private fun savePermissionRequest(permission: String) =
        pref.edit().putBoolean(permission, true).apply()
}