package com.example.learnanything.room.first_demo.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnanything.room.first_demo.data.source.database.entity.User
import com.example.learnanything.databinding.ItemUserListBinding

/**
 * Copyright © Monstar-lab Vietnam Co., Ltd.
 * Created by mvn-luc.vo-dn.
 */
class UserInfoAdapter(private var users: MutableList<User>) :
    RecyclerView.Adapter<UserInfoAdapter.UserInfoViewHolder>() {

    internal var onClickDetailPerson: (id: Int) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoViewHolder {
        val binding =
            ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserInfoViewHolder, position: Int) {
        holder.bindData(users[position])
    }

    override fun getItemCount() = users.size

    inner class UserInfoViewHolder(private val binding: ItemUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onClickDetailPerson.invoke(users[adapterPosition].id)
            }
        }
        fun bindData(user: User) {
            binding.tvIndex.text = user.id.toString()
            binding.tvPerson.text = user.name
        }
    }
}
