package com.example.weatherapp.utils.network

class UserException(
    val code: Int,
    message: String
) : Exception(message) {

    companion object {
        const val USER_NOT_FOUND = 1001
        const val PASSWORD_NOT_MATCH = 1002
    }

}
