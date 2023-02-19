package com.helpet


import android.content.IntentFilter.create
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import java.net.URI.create

interface VectorService {
    @Multipart
    @POST("/api/diagnosis/eye")
    fun vectorResult(
        @Part postImg: MultipartBody.Part,
//        @PartMap imgData: HashMap<String, RequestBody>
    ): Call<ResponseDto>

}
