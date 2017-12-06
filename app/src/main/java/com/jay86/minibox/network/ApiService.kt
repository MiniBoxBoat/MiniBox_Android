package com.jay86.minibox.network

import com.jay86.minibox.bean.ApiWrapper
import com.jay86.minibox.bean.User
import com.jay86.minibox.config.LOGIN
import com.jay86.minibox.config.REGISTER
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * api接口
 * Created by Jay on 2017/10/10.
 */

interface ApiService {
    @FormUrlEncoded
    @POST(LOGIN)
    fun login(@Field("phoneNumber") phone: String,
              @Field("password") pwd: String): Observable<ApiWrapper<User>>

    @FormUrlEncoded
    @POST(REGISTER)
    fun register(@Field("userName") nickname: String,
                 @Field("phoneNumber") phone: String,
                 @Field("password") password: String,
                 @Field("sex") gender: String): Observable<ApiWrapper<User>>
}