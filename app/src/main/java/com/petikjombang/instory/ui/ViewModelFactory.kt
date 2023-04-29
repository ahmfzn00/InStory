package com.petikjombang.instory.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.petikjombang.instory.injection.Injection
import com.petikjombang.instory.ui.add.AddStoryViewModels
import com.petikjombang.instory.ui.list.StoryViewModels
import com.petikjombang.instory.ui.location.LocationViewModels
import com.petikjombang.instory.ui.user.UserViewModels

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModels::class.java)) {
            return UserViewModels(Injection.loginInjection(context)) as T
        } else if (modelClass.isAssignableFrom(StoryViewModels::class.java)) {
            return StoryViewModels(Injection.storyInjection(context)) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModels::class.java)) {
            return AddStoryViewModels(Injection.storyInjection(context)) as T
        } else if (modelClass.isAssignableFrom(LocationViewModels::class.java)) {
            return LocationViewModels(Injection.storyInjection(context)) as T
        }

        throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
    }
}