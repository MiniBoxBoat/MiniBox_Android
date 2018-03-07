package com.jay86.minibox.network

import com.jay86.minibox.bean.CosResult
import com.jay86.minibox.config.UPLOAD_FILE
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created By jay68 on 2018/3/6.
 */
interface UploadService {
    @Multipart
    @POST(UPLOAD_FILE)
    fun uploadFile(@Header("Authorization") authorization: String,
                   @Path("filename") filename: String,
                   @Part("op") description: RequestBody,
                   @Part file: MultipartBody.Part): Observable<CosResult>
}