package com.example.newimagepicker
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Multipart

interface ImgBBApi {
    @Multipart
    @POST("1/upload")
    fun uploadImage(
        @Part("key") apiKey: String,
        @Part image: MultipartBody.Part
    ): Call<ImgBBResponse>
}