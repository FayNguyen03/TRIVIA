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
    val id: String,
    @SerialName("category")
    val category: String?,
    @SerialName("correctAnswer")
    val correctAnswer: String?,
    @SerialName("inAnswers")
    val inAnswers: String?,
    @SerialName("question")
    val question: String?,
    @SerialName("tags")
    val tags: String?,
    @SerialName("type")
    val type: String?,
    @SerialName("difficulty")
    val difficulty: String?
)

