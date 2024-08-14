package com.example.trivia_vivia

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="img_question_table")
class ImgQuestionEntity (
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name="question") var question:String?,
    @ColumnInfo(name="inAnswers") var inAnswers:String?,
    @ColumnInfo(name="correctAnswer") var correctAnswer:String?,
    @ColumnInfo(name="category") var category: String?,
    @ColumnInfo(name="tags") var tags: String?,
    @ColumnInfo(name="difficulty") var difficulty: String?
)