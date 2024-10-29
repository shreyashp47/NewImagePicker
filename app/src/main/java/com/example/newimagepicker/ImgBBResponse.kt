package com.example.newimagepicker

data class ImgBBResponse(
    val data: ImgData,
    val success: Boolean,
    val status: Int
)

data class ImgData(
    val url: String
)
