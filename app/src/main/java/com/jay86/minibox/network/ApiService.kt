package com.jay86.minibox.network

import com.jay86.minibox.bean.ApiWrapper
import com.jay86.minibox.bean.BoxGroup
import com.jay86.minibox.bean.ObjectApiWrapper
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
              @Field("password") pwd: String): Observable<ObjectApiWrapper<User>>

    @FormUrlEncoded
    @POST(REGISTER)
    fun register(@Field("userName") nickname: String,
                 @Field("phoneNumber") phone: String,
                 @Field("password") password: String,
                 @Field("sex") sex: String,
                 @Field("verifyCode") verifyCode: String): Observable<ObjectApiWrapper<User>>

    @FormUrlEncoded
    @POST(SEND_SMS)
    fun sendSms(@Field("phoneNumber") phoneNumber: String): Observable<ObjectApiWrapper<String>>

    @FormUrlEncoded
    @POST(SEARCH_BY_POINT)
    fun searchByPoint(@Field("lat") lat: Double, @Field("lng") lng: Double): Observable<ObjectApiWrapper<List<BoxGroup>>>

    @FormUrlEncoded
    @POST(SHOW_BOX_GROUP)
    fun searchByDestination(@Field("destination") destination: String): Observable<ObjectApiWrapper<List<BoxGroup>>>

    @GET(SHOW_BOX_GROUP)
    fun showBoxGroup(@Query("groupId") groupId: String): Observable<ObjectApiWrapper<BoxGroup>>

    @FormUrlEncoded
    @POST(APPOINT)
    fun appoint(@Field("userId") userId: String,
                @Field("userName") userName: String,
                @Field("phoneNumber") phoneNumber: String,
                @Field("groupId") groupId: String,
                @Field("boxSize") boxSize: String,
                @Field("openTime") openTime: String,
                @Field("useTime") useTime: String): Observable<ApiWrapper>

    @FormUrlEncoded
    @POST(ORDER)
    fun order(@Field("userId") userId: String,
              @Field("groupId") groupId: String,
              @Field("boxSize") boxSize: String,
              @Field("taken") token: String): Observable<ObjectApiWrapper<String>>
}