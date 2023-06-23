package com.example.weatherapp.user.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.auth.domain.model.User
import com.example.weatherapp.databinding.AdapterItemUserListBinding

class UserListAdapter(
    private val listener: UserListAdapterListener
) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
                    && oldItem.firstName == newItem.firstName
                    && oldItem.lastName == newItem.lastName
                    && oldItem.email == newItem.email
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(items: List<User>) = differ.submitList(items)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            binding = AdapterItemUserListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(differ.currentList[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class UserViewHolder(val binding: AdapterItemUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User) {

            binding.apply {

                tvFirstLastName.text = "${item.firstName} ${item.lastName}"

                tvEmail.text = item.email

                ivDelete.setOnClickListener {
                    listener.onDelete(item)
                }

                root.setOnClickListener {
                    listener.onUserClick(item)
                }
            }
        }
    }
}