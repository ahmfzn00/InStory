package com.petikjombang.instory.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.petikjombang.instory.data.local.entity.StoryEntity
import com.petikjombang.instory.data.repo.StoryRepository

class StoryViewModels(private val storyRepository: StoryRepository): ViewModel() {
    val story : LiveData<PagingData<StoryEntity>> = storyRepository.getStory().cachedIn(viewModelScope)

    fun getDetailStory(id: String) = storyRepository.getDetailStory(id)
}