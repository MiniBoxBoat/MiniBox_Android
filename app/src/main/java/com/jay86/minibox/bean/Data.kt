package com.jay86.minibox.bean

import com.google.gson.annotations.SerializedName

data class Data(@SerializedName("vid")
                val vid: String = "",
                @SerializedName("access_url")
                val accessUrl: String = "",
                @SerializedName("resource_path")
                val resourcePath: String = "",
                @SerializedName("source_url")
                val sourceUrl: String = "",
                @SerializedName("url")
                val url: String = "")