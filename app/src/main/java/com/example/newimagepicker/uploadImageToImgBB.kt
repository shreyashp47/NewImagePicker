package com.example.newimagepicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

fun uploadImageToImgBB(file: File) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.imgbb.com/")
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(ImgBBApi::class.java)

    // Create request body for image file
    val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
    val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

    val call = api.uploadImage("27f830b5525a342595cd9613c8b0a785", body)
    call.enqueue(object : retrofit2.Callback<ImgBBResponse> {
        override fun onResponse(call: Call<ImgBBResponse>, response: retrofit2.Response<ImgBBResponse>) {
            if (response.isSuccessful) {
                val imageUrl = response.body()?.data?.url
                println("Image uploaded successfully. URL: $imageUrl")
            } else {
                println("Upload failed. Response code: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<ImgBBResponse>, t: Throwable) {
            println("Upload failed: ${t.message}")
        }
    })
}
