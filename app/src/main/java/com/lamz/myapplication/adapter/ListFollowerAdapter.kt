package com.lamz.myapplication.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lamz.myapplication.databinding.ItemsFollowerBinding
import com.lamz.myapplication.remote.response.ItemsItem
import com.lamz.myapplication.ui.DetailActivity

class ListFollowerAdapter : ListAdapter<ItemsItem, ListFollowerAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsFollowerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.EXTRA_LOGIN, user.login)
          it.context.startActivity(intentDetail)
        }

    }

    class MyViewHolder(private val binding: ItemsFollowerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listUser: ItemsItem) {
            Glide.with(itemView)
                .load(listUser.avatarUrl)
                .into(binding.imgFollower)
            binding.tvFollower.text = listUser.login
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}