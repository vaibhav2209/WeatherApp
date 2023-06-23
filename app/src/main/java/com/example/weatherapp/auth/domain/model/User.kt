package com.example.weatherapp.auth.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val passWord: String = ""
)
