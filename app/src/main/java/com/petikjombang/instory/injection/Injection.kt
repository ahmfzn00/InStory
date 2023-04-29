package com.petikjombang.instory.injection

import android.content.Context
import com.petikjombang.instory.data.local.preferences.UserPreference
import com.petikjombang.instory.data.remote.network.ApiConfig
import com.petikjombang.instory.data.remote.network.ApiService
import com.petikjombang.instory.data.repo.StoryRepository
import com.petikjombang.instory.data.repo.UserRepository

object Injection {

    fun loginInjection(context: Context) : UserRepository {
        val apiService = ApiConfig().getApiService("")

        return UserRepository(apiService)
    }

    fun storyInjection(context: Context) : StoryRepository {
        val token = UserPreference(context).getUser().token
        val apiService = ApiConfig().getApiService(token.toString())

        return StoryRepository(apiService)
    }
}