package com.example.learnanything.activity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import com.example.learnanything.R

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class DefaultDialogFragment : DialogFragment() {

    companion object {
        fun newInstance() = DefaultDialogFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(context, R.style.AlertDialogCustom)
            .setMessage("are you sure you want to delete this entry?")
            .setPositiveButton("OK") { _, _ -> }
            .create()
}