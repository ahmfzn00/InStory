package com.petikjombang.instory.ui.add

import androidx.lifecycle.ViewModel
import com.petikjombang.instory.data.repo.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModels(private val storyRepository: StoryRepository): ViewModel() {
    fun addStory(imageMultipartBody: MultipartBody.Part, desc: RequestBody) = storyRepository.addStory(imageMultipartBody, desc)
}