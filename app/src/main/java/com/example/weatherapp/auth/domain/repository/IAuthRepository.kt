package com.example.weatherapp.auth.domain.repository

import com.example.weatherapp.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {


    fun getUserById(id: Int): Flow<User>

    fun getAllUser(): Flow<List<User>>

    suspend fun updateUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun addUser(user: User)

    fun isUserExists(email: String): Flow<User?>
}