package com.petikjombang.instory.data.remote.network

import com.petikjombang.instory.data.local.entity.StoryEntity
import com.petikjombang.instory.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("stories")
    suspend fun getStory(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ) : StoryResponse

    @GET("stories")
    fun getStoryForTesting() : StoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String
    ) : DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ) : FileUploadResponse

    @GET("stories?location=1")
    suspend fun getLocationStory() : LocationStoryResponse
}