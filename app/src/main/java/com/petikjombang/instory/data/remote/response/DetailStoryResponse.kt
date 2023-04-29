package com.petikjombang.instory.data.remote.response

import com.google.gson.annotations.SerializedName
import com.petikjombang.instory.data.local.entity.StoryEntity

data class DetailStoryResponse (
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("story")
    val story: StoryEntity
    )