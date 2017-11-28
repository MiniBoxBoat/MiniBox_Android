package com.jay86.minibox.bean

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Created By jay68 on 2017/11/21.
 */

data class ApiWrapper<out T>(val status: String, val message: String,
                             @SerializedName("object") val data: T)

data class User(@SerializedName("userId") val id: String,
                @SerializedName("userName") var nickname: String,
                @SerializedName("image") var avatar: String,
                var realName: String,
                var phoneNumber: String,
                var age: String,
                var email: String,
                var credibility: String,
                var useTime: String,
                var sex: String,
                var password: String) {
    companion object {
        val ACCOUNT_MIN_LENGTH = 6
        val PASSWORD_MIN_LENGTH = 6
        fun toJson(user: User): String = Gson().toJson(user)
        fun fromJson(json: String): User? = Gson().fromJson(json, User::class.java)
    }

    override fun toString(): String {
        return toJson(this)
    }
}