package com.lamz.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lamz.myapplication.R
import com.lamz.myapplication.database.UserEntity
import com.lamz.myapplication.databinding.ItemsUserBinding
import com.lamz.myapplication.ui.DetailActivity


class ListProfileAdapter(private val onFavoriteClick : (UserEntity)->Unit) : ListAdapter<UserEntity, ListProfileAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemsUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        val ivAdd = holder.binding.ivAdd
        if (user.isFavorite){
            ivAdd.setImageDrawable(ContextCompat.getDrawable(ivAdd.context, R.drawable.ic_favorited))
        }else{
            ivAdd.setImageDrawable(ContextCompat.getDrawable(ivAdd.context, R.drawable.ic_favorite))
        }
        ivAdd.setOnClickListener{

            onFavoriteClick(user)
        }
    }

    class MyViewHolder(val binding: ItemsUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listUser: UserEntity) {
            binding.tvUser.text = listUser.username
            Glide.with(itemView)
                .load(listUser.avatarUrl)
                .into(binding.imgUser)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_LOGIN, listUser.username)
                itemView.context.startActivity(intent)
            }

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

}