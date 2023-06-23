package com.example.weatherapp.auth.data.repository

import com.example.weatherapp.auth.data.local.AuthDao
import com.example.weatherapp.auth.domain.model.User
import com.example.weatherapp.auth.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authDao: AuthDao
): IAuthRepository {

    override fun getUserById(id: Int) : Flow<User>  =
        authDao.getUserById(id)

    override fun isUserExists(email: String) : Flow<User?>  =
        authDao.isUserExists(email)

    override fun getAllUser() : Flow<List<User>> =
        authDao.getAllUser()

    override suspend fun updateUser(user: User) =
        authDao.updateUser(user)

    override suspend fun deleteUser(user: User) =
        authDao.deleteUser(user)

    override suspend fun addUser(user: User) =
        authDao.addUser(user)
}