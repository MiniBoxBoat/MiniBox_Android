package com.jay86.minibox.network

import com.jay86.minibox.bean.*
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
    @POST(UPDATE_USER_INFO)
    fun updateUserInfo(@Field("userName") userName: String,
                       @Field("phoneNumber") phone: String,
                       @Field("email") email: String,
                       @Field("sex") sex: String,
                       @Field("taken") token: String,
                       @Field("trueName") trueName: String): Observable<ObjectApiWrapper<User>>

    @FormUrlEncoded
    @POST(UPDATE_AVATAR)
    fun updateAvatar(@Field("taken") token: String,
                     @Field("avatarUrl") url: String): Observable<ApiWrapper>

    @FormUrlEncoded
    @POST(RESET_PASSWORD)
    fun resetPassword(@Field("newPassword") password: String,
                      @Field("verifyCode") verifyCode: String,
                      @Field("taken") token: String): Observable<ApiWrapper>

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
    fun appoint(@Field("userName") userName: String,
                @Field("phoneNumber") phoneNumber: String,
                @Field("groupId") groupId: String,
                @Field("boxSize") boxSize: String,
                @Field("openTime") openTime: String,
                @Field("useTime") useTime: String,
                @Field("boxNum") boxNum: String,
                @Field("taken") token: String): Observable<ApiWrapper>

    @FormUrlEncoded
    @POST(ORDER)
    fun order(@Field("groupId") groupId: String,
              @Field("boxSize") boxSize: String,
              @Field("taken") token: String,
              @Field("boxNum") boxNum: String): Observable<ObjectApiWrapper<List<String>>>

//    @FormUrlEncoded
    @GET(SHOW_USING_BOX)
    fun showUsingBox(@Query("taken") token: String): Observable<ObjectApiWrapper<List<Box>>>

//    @FormUrlEncoded
    @GET(SHOW_APPOINTING_BOX)
    fun showAppointingBox(@Query("taken") token: String): Observable<ObjectApiWrapper<List<Box>>>

    @FormUrlEncoded
    @POST(CONVERT_APPOINT_TO_ORDER)
    fun convertAppointToOrder(@Field("reservationId") reservationId: String,
                              @Field("taken") token: String): Observable<ApiWrapper>

    @FormUrlEncoded
    @POST(END_ORDER)
    fun endOrder(@Field("orderId") reservationId: String,
                 @Field("cost") token: String): Observable<ApiWrapper>
}