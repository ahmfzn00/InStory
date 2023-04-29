package com.petikjombang.instory.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityOptionsCompat
import com.petikjombang.instory.R
import com.petikjombang.instory.data.local.preferences.UserPreference
import com.petikjombang.instory.data.remote.response.LoginResult
import com.petikjombang.instory.databinding.ActivitySplashScreenBinding
import com.petikjombang.instory.ui.list.MainActivity
import com.petikjombang.instory.ui.user.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var splashScreenBinding: ActivitySplashScreenBinding
    private var user: LoginResult = LoginResult()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding.root)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            showExisting()
        }, DELAY_TIME)
    }

    private fun showExisting() {
        user = UserPreference(this).getUser()
        val token = user.token
        if (token?.isNotEmpty()!!) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(this@SplashScreenActivity).toBundle())
            finish()
        } else {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(this@SplashScreenActivity).toBundle())
            finish()
        }
    }

    companion object {
        const val DELAY_TIME = 3500L
    }
}