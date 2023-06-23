package com.example.weatherapp.auth.data.local

import androidx.room.*
import com.example.weatherapp.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthDao {

    @Query("SELECT * from User Where userId =:id")
    fun getUserById(id: Int) : Flow<User>

    @Query("SELECT * FROM User WHERE email =:email LIMIT 1")
    fun isUserExists(email: String) : Flow<User?>

    @Query("SELECT * from User")
    fun getAllUser() : Flow<List<User>>

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)
}