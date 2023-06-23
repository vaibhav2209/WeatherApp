package com.example.weatherapp.app

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.auth.data.local.AuthDao
import com.example.weatherapp.auth.domain.model.User

@Database(
    entities = [User::class] ,
    version = 1,
    exportSchema = true
)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun authDao(): AuthDao

    companion object {
        const val LOCAL_DATABASE = "LOCAL_DATABASE"
    }
}