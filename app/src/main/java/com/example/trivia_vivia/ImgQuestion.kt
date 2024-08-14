package com.example.trivia_vivia

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class ImgQuestion(
    val category: String,
    val id: String,
    val tags: List<String>,
    val difficulty: String,
    val question: String,
    val correctAnswer: List<ImageAnswer>,
    val incorrectAnswers: List<List<ImageAnswer>>,
    val type: String
)



data class ImageAnswer(
    val url: String,
    val height: Int,
    val width: Int,
    val size: Int,
    val author: Author,
    val source: Source,
    val description: String,
    val license: License
)

data class Author(
    val name: String,
    val url: String
)

data class Source(
    val url: String
)

data class License(
    val url: String,
    val name: String
)