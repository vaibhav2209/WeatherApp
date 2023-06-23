package com.example.weatherapp.user.presentation.adapter

import com.example.weatherapp.auth.domain.model.User

interface UserListAdapterListener {

    fun onUserClick(user: User)

    fun onDelete(user: User)
}