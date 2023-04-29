package com.petikjombang.instory.ui.location

import androidx.lifecycle.ViewModel
import com.petikjombang.instory.data.repo.StoryRepository

class LocationViewModels(private val storyRepository: StoryRepository): ViewModel() {
    fun getLocationStory() = storyRepository.getLocationStory()
}