package com.jay86.minibox.bean

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Created By jay68 on 2017/11/21.
 */

data class ApiWrapper<out T>(val status: String,
                             val message: String,
                             @SerializedName("object") val data: T)

data class User(@SerializedName("taken") val token: String,
                @SerializedName("userId") val id: String,
                @SerializedName("userName") var nickname: String,
                @SerializedName("image") var avatar: String,
                var phoneNumber: String,
                var trueName: String,
                var age: Int?,
                var email: String,
                var credibility: Int?,
                var useTime: Int?,
                var sex: String,
                var password: String) {
    companion object {
        val PASSWORD_LENGTH = 6..20
        val NICKNAME_LENGTH = 2..8
        fun toJson(user: User): String = Gson().toJson(user)
        fun fromJson(json: String): User? = Gson().fromJson(json, User::class.java)
    }

    override fun toString(): String {
        return toJson(this)
    }
}