package com.example.weatherapp.utils.network

sealed class Resource<out R> {

    data class Success<out R>(val result: R): Resource<R>()

    data class Failure(val exception: Exception): Resource<Nothing>()

    object Loading: Resource<Nothing>()

    object NoResult: Resource<Nothing>()
}