package com.petikjombang.instory.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterItems(
    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("name")
    val name: String? = null
) : Parcelable

@Parcelize
data class RegisterResponse(
    @field:SerializedName("registerItems")
    val registerItems: RegisterItems,

    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable

@Parcelize
data class LoginResult(
    @field:SerializedName("userId")
    var userId: String? = null,

    @field:SerializedName("token")
    var token: String? = null,

    @field:SerializedName("name")
    var name: String? = null
) : Parcelable

@Parcelize
data class LoginResponse(
    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("loginResult")
    val loginResult: LoginResult
) : Parcelable