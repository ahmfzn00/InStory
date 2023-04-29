package com.petikjombang.instory.data.repo

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.petikjombang.instory.data.remote.network.ApiService
import com.petikjombang.instory.data.remote.response.LoginResult
import com.petikjombang.instory.data.remote.response.RegisterItems

class UserRepository(
    private val apiService: ApiService
) {
    fun loginAction(email: String, pass: String): LiveData<com.petikjombang.instory.data.Result<LoginResult>> = liveData {
        emit(com.petikjombang.instory.data.Result.Loading)
        try {
            val response = apiService.loginUser(email, pass)
            val user = response.loginResult
            emit(com.petikjombang.instory.data.Result.Success(user))

        } catch (e: Exception) {
            Log.e("User Repository", e.message.toString())
            emit(com.petikjombang.instory.data.Result.Error(e.message.toString()))
        }
    }

    fun registerAction(email: String, username: String, pass: String): LiveData<com.petikjombang.instory.data.Result<RegisterItems>> = liveData {
        emit(com.petikjombang.instory.data.Result.Loading)
        try {
            val response = apiService.registerUser(username, email, pass)
            val user = response.registerItems
            emit(com.petikjombang.instory.data.Result.Success(user))
        } catch (e: Exception) {
            Log.e("User Repository", e.message.toString())
            emit(com.petikjombang.instory.data.Result.Error(e.message.toString()))
        }
    }
}