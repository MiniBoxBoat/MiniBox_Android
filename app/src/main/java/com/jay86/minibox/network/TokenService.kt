package com.jay86.minibox.network

import com.jay86.minibox.bean.TokenWrapper
import com.jay86.minibox.config.GET_TOKEN
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created By jay68 on 2018/3/7.
 */
interface TokenService {
    @GET(GET_TOKEN)
    fun getToken(): Observable<TokenWrapper>
}