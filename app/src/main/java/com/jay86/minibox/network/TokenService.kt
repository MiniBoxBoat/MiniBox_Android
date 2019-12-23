package com.jay86.minibox.network

import com.jay86.minibox.bean.TokenWrapper
import com.jay86.minibox.config.GET_TOKEN
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by Jay
 */
interface TokenService {
    @GET(GET_TOKEN)
    fun getToken(): Observable<TokenWrapper>
}