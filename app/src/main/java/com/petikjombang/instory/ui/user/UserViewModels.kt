package com.petikjombang.instory.ui.user

import androidx.lifecycle.ViewModel
import com.petikjombang.instory.data.repo.UserRepository

class UserViewModels(private val userRepository: UserRepository) : ViewModel() {
    fun loginAction(email: String, pass: String) = userRepository.loginAction(email, pass)
    fun registerAction(email: String, username: String, pass: String) = userRepository.registerAction(email, username, pass)

}