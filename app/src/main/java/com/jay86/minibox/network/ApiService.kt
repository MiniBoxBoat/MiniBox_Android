package com.jay86.minibox.network

import com.jay86.minibox.bean.ApiWrapper
import com.jay86.minibox.bean.BoxGroup
import com.jay86.minibox.bean.User
import com.jay86.minibox.config.*
import io.reactivex.Observable
import retrofit2.http.*

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

    @FormUrlEncoded
    @POST(SEARCH_BY_POINT)
    fun searchByPoint(@Field("lat") lat: Double, @Field("lng") lng: Double): Observable<ApiWrapper<List<BoxGroup>>>

    @FormUrlEncoded
    @POST(SHOW_BOX_GROUP)
    fun searchByDestination(@Field("destination") destination: String): Observable<ApiWrapper<List<BoxGroup>>>

    @GET(SHOW_BOX_GROUP)
    fun showBoxGroup(@Query("groupId") groupId: String): Observable<ApiWrapper<BoxGroup>>
}