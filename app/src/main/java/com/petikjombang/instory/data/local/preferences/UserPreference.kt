package com.petikjombang.instory.data.local.preferences

import android.content.Context
import com.petikjombang.instory.data.remote.response.LoginResult

class UserPreference(context: Context) {
    companion object {
        private const val PREF_NAME = "user_pref"
        private const val NAME = "name"
        private const val TOKEN = "token"
        private const val USER_ID = "user_id"
    }

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setUser(user : LoginResult) {
        val editUser = preferences.edit()
        editUser.putString(NAME, user.name)
        editUser.putString(TOKEN, user.token)
        editUser.putString(USER_ID, user.userId)

        editUser.apply()
    }

    fun getUser(): LoginResult {
        val modelUser = LoginResult()
        modelUser.name = preferences.getString(NAME, "")
        modelUser.token = preferences.getString(TOKEN, "")
        modelUser.userId = preferences.getString(USER_ID, "")

        return modelUser
    }
}