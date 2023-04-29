package com.petikjombang.instory.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.petikjombang.instory.data.Result
import com.petikjombang.instory.data.StoryPagingSource
import com.petikjombang.instory.data.local.entity.StoryEntity
import com.petikjombang.instory.data.remote.network.ApiService
import com.petikjombang.instory.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart

class StoryRepository(
    private val apiService: ApiService,
) {

    fun getStory(): LiveData<PagingData<StoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    fun getStoryForTesting(): LiveData<com.petikjombang.instory.data.Result<StoryResponse>> = liveData {
        emit(com.petikjombang.instory.data.Result.Loading)
        try {
            val response = apiService.getStoryForTesting()
            emit(com.petikjombang.instory.data.Result.Success(response))

        } catch (e: Exception) {
            Log.e("Get Story", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailStory(id: String): LiveData<com.petikjombang.instory.data.Result<StoryEntity>> = liveData {
        emit(com.petikjombang.instory.data.Result.Loading)
        try {
            val response = apiService.getDetailStory(id)
            val data = response.story

            emit(com.petikjombang.instory.data.Result.Success(data))
        } catch (e: Exception) {
            Log.e("Get Detail Story", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun addStory(imageMultipart: MultipartBody.Part, desc: RequestBody) : LiveData<com.petikjombang.instory.data.Result<FileUploadResponse>> = liveData{
        emit(com.petikjombang.instory.data.Result.Loading)
        try {
            val response = apiService.addStory(imageMultipart, desc)
            emit(com.petikjombang.instory.data.Result.Success(response))
        } catch (e: Exception) {
            Log.e("Add Story", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }

    }

    fun getLocationStory() : LiveData<com.petikjombang.instory.data.Result<LocationStoryResponse>> = liveData {
        emit(com.petikjombang.instory.data.Result.Loading)
        try {
            val response = apiService.getLocationStory()
            val data = response.listStoryLocation
            emit(com.petikjombang.instory.data.Result.Success(response))
        } catch (e: Exception) {
            Log.e("Add Story", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

}