package com.jay86.minibox.network

import com.jay86.minibox.bean.ApiWrapper
import com.jay86.minibox.bean.User
import com.jay86.minibox.config.LOGIN
import com.jay86.minibox.config.REGISTER
import com.jay86.minibox.config.SEND_SMS
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
                 @Field("sex") sex: String,
                 @Field("verifyCode") verifyCode: String): Observable<ApiWrapper<User>>

    @FormUrlEncoded
    @POST(SEND_SMS)
    fun sendSms(@Field("phoneNumber") phoneNumber: String): Observable<ApiWrapper<String>>
}