package com.jay86.minibox.network

import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.jay86.minibox.App
import com.jay86.minibox.BuildConfig
import com.jay86.minibox.bean.*
import com.jay86.minibox.config.BASE_TOKEN_URL
import com.jay86.minibox.config.BASE_UPLOAD_URL
import com.jay86.minibox.config.BASE_URL
import com.jay86.minibox.network.observer.BaseObserver
import com.jay86.minibox.utils.LocationUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 网络请求框架
 * Created by Jay on 2017/10/10.
 */
object RequestManager {
    private const val REQUEST_SUCCESSFUL = "200"
    private const val DEFAULT_TIME_OUT = 30

    private val apiService: ApiService
    private val uploadService: UploadService
    private val tokenService: TokenService

    init {
        val client = configureOkHttp(OkHttpClient.Builder())
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        apiService = retrofit.create(ApiService::class.java)

        val uploadRetrofit = Retrofit.Builder()
                .baseUrl(BASE_UPLOAD_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        uploadService = uploadRetrofit.create(UploadService::class.java)

        val tokenRetrofit = Retrofit.Builder()
                .baseUrl(BASE_TOKEN_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        tokenService = tokenRetrofit.create(TokenService::class.java)
    }

    private fun configureOkHttp(builder: OkHttpClient.Builder): OkHttpClient {
        builder.connectTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }
        return builder.build()
    }

    fun login(phone: String, password: String, observer: BaseObserver<User>) {
        apiService.login(phone, password)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun register(nickname: String, phone: String, password: String, sex: String, verifyCode: String, observer: BaseObserver<User>) {
        apiService.register(nickname, phone, password, sex, verifyCode)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun updateUserInfo(userName: String, phone: String, email: String, sex: String, token: String, trueName: String, observer: BaseObserver<User>) {
        apiService.updateUserInfo(userName, phone, email, sex, token, trueName)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun updateAvatar(token: String, filename: String, description: RequestBody, file: MultipartBody.Part, observer: BaseObserver<Unit>) {
        tokenService.getToken()
                .flatMap { uploadService.uploadFile(it.data, filename, description, file) }
                .flatMap {
                    if (it.code == 0) {
                        App.user!!.avatar = it.data.sourceUrl
                        return@flatMap apiService.updateAvatar(token, it.data.sourceUrl)
                    } else {
                        throw ApiException(it.code.toString(), it.message)
                    }
                }
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun resetPassword(password: String, verifyCode: String, token: String, observer: Observer<Unit>) {
        apiService.resetPassword(password, verifyCode, token)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun sendSms(phoneNumber: String, observer: BaseObserver<String>) {
        apiService.sendSms(phoneNumber)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun searchByDestination(destination: String, observer: Observer<List<BoxGroup>>) {
        apiService.searchByDestination(destination)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun searchByPoint(lat: Double, lng: Double, observer: Observer<List<BoxGroup>>) {
        apiService.searchByPoint(lat, lng)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun showBoxGroup(groupId: String, observer: Observer<BoxGroup>) {
        apiService.showBoxGroup(groupId)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun appoint(userId: String, userName: String, phoneNumber: String, groupId: String,
                boxSize: String, openTime: String, useTime: String, boxNum: String, token: String, observer: Observer<Unit>) {
        apiService.appoint(userName, phoneNumber, groupId, boxSize, openTime, useTime, boxNum, token)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun order(userId: String, groupId: String, boxSize: String, token: String, boxNum: String, observer: Observer<List<String>>) {
        apiService.order(groupId, boxSize, token, boxNum)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun showUsingBox(token: String, observer: Observer<List<Box>>) {
        apiService.showUsingBox(token)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun showAppointingBox(token: String, observer: Observer<List<Box>>) {
        apiService.showAppointingBox(token)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun convertAppointToOrder(reservationId: String, token: String, observer: Observer<Unit>) {
        apiService.convertAppointToOrder(reservationId, token)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    fun endOrder(orderId: String, cost: String, observer: Observer<Unit>) {
        apiService.endOrder(orderId, cost)
                .map { it.nextOrError() }
                .subscriber(observer)
    }

    private fun <T> ObjectApiWrapper<T>.nextOrError() =
            if (status != REQUEST_SUCCESSFUL) throw ApiException(status, message) else data

    private fun ApiWrapper.nextOrError() =
            if (status != REQUEST_SUCCESSFUL) throw ApiException(status, message) else Unit

    private fun <T> Observable<T>.subscriber(observer: Observer<T>) {
        subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }
}