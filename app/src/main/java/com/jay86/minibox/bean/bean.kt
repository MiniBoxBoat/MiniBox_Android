package com.jay86.minibox.bean

/**
 * Created By jay68 on 2017/11/21.
 */

data class ApiWrapper<out T>(val status: String, val message: String, val data: T)

data class User(val id: String, var nickname: String, var avatar: String) {
    companion object {
        val ACCOUNT_MIN_LENGTH = 6
        val PASSWORD_MIN_LENGTH = 6
    }
}