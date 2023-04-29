package com.petikjombang.instory.data.dummy

import com.petikjombang.instory.data.local.entity.StoryEntity
import com.petikjombang.instory.data.remote.response.StoryResponse

object DataDummy {
    fun generateStory(): StoryResponse {
        val storyList = ArrayList<StoryEntity>()
        for (i in 0..20) {
            val story = StoryEntity(
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-02-22T22:22:22Z",
                "name $i",
                "desc $i",
                i.toDouble()*10,
                "id_$i",
                i.toDouble()*10
                )
            storyList.add(story)
        }

        return StoryResponse(
            storyList,
            false,
            "Berhasil"
        )
    }
}