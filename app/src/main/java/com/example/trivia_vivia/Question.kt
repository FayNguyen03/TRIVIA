package com.example.trivia_vivia


//Denotes that the annotated element should not be removed when the code is minified at build time
import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class QuestionsResponse(
    @SerialName("questions")
    val questions: List<Question>?
)

@Keep
@Serializable
data class Question(
    @SerialName("id")
    val id: String?,
    @SerialName("category")
    val category: String?,
    @SerialName("correctAnswer")
    val correctAnswer: String?,
    @SerialName("incorrectAnswers")
    val incorrectAnswers: List<String>?,
    @SerialName("question")
    val question: Int?,
    @SerialName("tags")
    val tags: List<String>?,
    @SerialName("type")
    val type: String?,
    @SerialName("difficulty")
    val difficulty: String?,
    @SerialName("isNiche")
    val isNiche: Boolean?
)

