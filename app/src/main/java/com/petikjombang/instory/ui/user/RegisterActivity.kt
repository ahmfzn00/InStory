package com.petikjombang.instory.ui.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.petikjombang.instory.R
import com.petikjombang.instory.databinding.ActivityRegisterBinding
import com.petikjombang.instory.ui.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    private val userViewModels : UserViewModels by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        supportActionBar?.hide()

        registerBinding.btnRegister.setOnClickListener {
            registerAction(userViewModels)
        }

        registerBinding.tvRegisterToLogin.setOnClickListener {
            actionToLogin()
        }


    }

    private fun actionToLogin() {
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun registerAction(userViewModels: UserViewModels) {
        val email = registerBinding.etRegEmail.text.toString().trim()
        val username = registerBinding.etRegUsername.text.toString()
        val pass = registerBinding.etRegPass.text.toString().trim()

        if (email != null && username != null && pass != null) {
            try {
                userViewModels.registerAction(email, username, pass).observe(this){ result ->
                    if (result != null) {
                        when(result) {
                            is com.petikjombang.instory.data.Result.Loading -> {
                                registerBinding.regProgressBar.visibility = View.VISIBLE
                            }

                            is com.petikjombang.instory.data.Result.Success -> {
                                registerBinding.regProgressBar.visibility = View.GONE
                                val data = result.data
                                Toast.makeText(this, "Berhasil Register ${data}", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }

                            is com.petikjombang.instory.data.Result.Error -> {
                                registerBinding.regProgressBar.visibility = View.GONE
                                Toast.makeText(this, "Gagal Register", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Gagal Register : ${e.message.toString()}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Lengkapi Form Register", Toast.LENGTH_SHORT).show()

        }
    }
}