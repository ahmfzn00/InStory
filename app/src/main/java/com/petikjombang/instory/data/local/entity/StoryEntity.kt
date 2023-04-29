package com.petikjombang.instory.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
class StoryEntity (

        @field:ColumnInfo(name = "photoUrl")
        val photoUrl: String? = null,

        @field:ColumnInfo(name = "createdAt")
        val createdAt: String? = null,

        @field:ColumnInfo(name = "name")
        val name: String? = null,

        @field:ColumnInfo(name = "description")
        val description: String? = null,

        @field:ColumnInfo(name = "lon")
        val lon: Double? = null,

        @field:PrimaryKey
        @field:ColumnInfo(name = "id")
        val id: String,

        @field:SerializedName("lat")
        val lat: Double? = null

) : Parcelable