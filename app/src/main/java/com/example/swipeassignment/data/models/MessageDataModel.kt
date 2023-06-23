package com.example.swipeassignment.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class MessageDataModel(@Json(name = "message") val message: String)