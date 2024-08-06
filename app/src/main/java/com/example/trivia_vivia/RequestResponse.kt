package com.example.trivia_vivia

import androidx.annotation.Keep

@Keep
data class RequestResponse (
    val error: String,
    val response: String
)