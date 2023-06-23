package com.example.weatherapp.auth.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.auth.domain.model.User
import com.example.weatherapp.auth.domain.repository.IAuthRepository
import com.example.weatherapp.utils.network.Resource
import com.example.weatherapp.utils.network.UserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: IAuthRepository
) : ViewModel() {

    /*Get user by Id*/
    private val userById = MutableLiveData<Resource<User>>()

    fun getUserById(id: Int) =
        authRepo.getUserById(id = id)
            .onStart { userById.postValue(Resource.Loading) }
            .onEach { u -> userById.postValue(Resource.Success(u)) }
            .flowOn(Dispatchers.IO)
            .catch { e -> userById.postValue(Resource.Failure(e as Exception)) }
            .launchIn(viewModelScope)

    fun doObserverGetUserById(): LiveData<Resource<User>> =
        userById

    /*Login user */
    private val loginUser = MutableLiveData<Resource<User>>()

    fun getLoginViaEmail(email: String, pass: String) {
        authRepo.isUserExists(email)
            .onStart { loginUser.postValue(Resource.Loading) }
            .onEach { u ->
                if (u != null) {
                    if (u.passWord == pass)
                        loginUser.postValue(Resource.Success(u))
                    else
                        loginUser.postValue(Resource.Failure(
                            UserException(
                                UserException.PASSWORD_NOT_MATCH,
                                "Password does not match"
                            )
                        ))
                } else
                    loginUser.postValue(Resource.NoResult)
            }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .catch { e -> loginUser.postValue(Resource.Failure(e as Exception)) }
            .launchIn(viewModelScope)
    }

    fun doObserverGetLoginViaEmail(): LiveData<Resource<User>> =
        loginUser

    /*All user */
    private val allUsers = MutableLiveData<Resource<List<User>>>()

    fun getAllUser() =
        authRepo.getAllUser()
            .onStart { allUsers.postValue(Resource.Loading) }
            .onEach { u ->
                if (u.isEmpty())
                    allUsers.postValue(Resource.NoResult)
                else
                    allUsers.postValue(Resource.Success(u))
            }
            .flowOn(Dispatchers.IO)
            .catch { e -> allUsers.postValue(Resource.Failure(e as Exception)) }
            .launchIn(viewModelScope)

    fun doObserverGetAllUser(): LiveData<Resource<List<User>>> =
        allUsers

    /*update user */
    fun updateUser(user: User) {
        viewModelScope.launch {
            authRepo.updateUser(user)
        }
    }

    /*add user */
    fun addUser(user: User) {
        viewModelScope.launch {
            authRepo.addUser(user)
        }
    }

    /*delete user */
    fun deleteUser(user: User) {
        viewModelScope.launch {
            authRepo.deleteUser(user)
        }
    }

}