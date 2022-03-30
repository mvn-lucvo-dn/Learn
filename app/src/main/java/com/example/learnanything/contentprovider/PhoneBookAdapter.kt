package com.example.learnanything.contentprovider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnanything.databinding.ItemPhonebookBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class PhoneBookAdapter(private val phoneBooks: MutableList<PhoneBookInfo>) :
    RecyclerView.Adapter<PhoneBookAdapter.PhoneBookViewHolder>() {

    internal var onMakeCall: () -> Unit = {}
    private var phoneNumber = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneBookViewHolder {
        return PhoneBookViewHolder(
            ItemPhonebookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PhoneBookViewHolder, position: Int) {
        holder.onBindData(phoneBooks[position])
    }

    override fun getItemCount() = phoneBooks.size

    internal fun getPhoneNumberSelect() = phoneNumber

    inner class PhoneBookViewHolder(val binding: ItemPhonebookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imgPhone.setOnClickListener {
                phoneNumber = phoneBooks[adapterPosition].phoneNumber
                onMakeCall.invoke()
            }
        }

        fun onBindData(phoneBookInfo: PhoneBookInfo) {
            with(binding) {
                tvName.text = phoneBookInfo.userName
                tvPhone.text = phoneBookInfo.phoneNumber.toString()
            }
        }
    }
}