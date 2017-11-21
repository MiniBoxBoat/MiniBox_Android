package com.jay86.minibox.bean

/**
 * Created By jay68 on 2017/11/21.
 */

data class ApiWrapper<out T>(val status: String, val message: String, val data: T)