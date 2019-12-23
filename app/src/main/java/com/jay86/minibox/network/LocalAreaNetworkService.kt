package com.jay86.minibox.network

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Jay
 */
interface LocalAreaNetworkService {
    @GET("on")
    fun openBox(): Call<String>

    @GET("off")
    fun closeBox(): Call<String>
}