package com.example.weatherapp.utils.network

import java.util.regex.Pattern

object TextUtils {

    const val EMAIL_PATTERN = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"

    fun String.isValidEmail() =
        Pattern.compile(EMAIL_PATTERN).matcher(this).matches()

}