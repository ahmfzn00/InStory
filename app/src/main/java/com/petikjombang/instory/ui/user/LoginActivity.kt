package com.petikjombang.instory.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.petikjombang.instory.R
import com.petikjombang.instory.data.local.preferences.UserPreference
import com.petikjombang.instory.data.remote.response.LoginResult
import com.petikjombang.instory.databinding.ActivityLoginBinding
import com.petikjombang.instory.ui.ViewModelFactory
import com.petikjombang.instory.ui.list.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private var userLogin: LoginResult = LoginResult()
    private val userViewModels : UserViewModels by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        supportActionBar?.hide()

        loginBinding.btnLogin.setOnClickListener {
            loginActionUser(userViewModels)
        }

        loginBinding.tvLoginCtaRegister.setOnClickListener {
            actionToRegister()
        }
    }

    private fun actionToRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun loginActionUser(userViewModels: UserViewModels) {
        val email = loginBinding.etLoginEmail.text.toString().trim()
        val pass = loginBinding.etLoginPass.text.toString().trim()

        if (email != null && pass != null) {
            try {
                userViewModels.loginAction(email, pass).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is com.petikjombang.instory.data.Result.Loading -> {
                                loginBinding.loginProgressBar.visibility = View.VISIBLE
                            }

                            is com.petikjombang.instory.data.Result.Success -> {
                                val data = result.data
                                val userPreference = UserPreference(this)
                                userLogin.let { user ->
                                    user.userId = data.userId
                                    user.name = data.name
                                    user.token = data.token
                                }
                                userPreference.setUser(userLogin)
                                loginBinding.loginProgressBar.visibility = View.GONE
                                Toast.makeText(this, "Berhasil Login", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()

                            }
                            is com.petikjombang.instory.data.Result.Error -> {
                                loginBinding.loginProgressBar.visibility = View.GONE
                                Toast.makeText(this, "Gagal Login", Toast.LENGTH_SHORT).show()

                            }
                        }
                    }

                }
            } catch (e: Exception) {
                Toast.makeText(this, "Gagal Login : ${e.message.toString()}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Lengkapi Form Login", Toast.LENGTH_SHORT).show()
        }
    }
}