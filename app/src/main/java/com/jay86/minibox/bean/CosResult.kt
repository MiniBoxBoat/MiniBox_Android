package com.jay86.minibox.bean

import com.google.gson.annotations.SerializedName

data class CosResult(@SerializedName("code")
                     val code: Int = 0,
                     @SerializedName("data")
                     val data: Data,
                     @SerializedName("message")
                     val message: String = "",
                     @SerializedName("request_id")
                     val requestId: String = "")