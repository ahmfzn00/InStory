package com.petikjombang.instory.data.remote.response

import com.google.gson.annotations.SerializedName
import com.petikjombang.instory.data.local.entity.StoryEntity

data class StoryResponse (
    @field:SerializedName("listStory")
    val listStory: List<StoryEntity>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)