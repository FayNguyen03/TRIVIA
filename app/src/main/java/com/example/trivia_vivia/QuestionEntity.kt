package com.example.trivia_vivia

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//table in database
@Entity(tableName="question_table")
data class QuestionEntity (
    @PrimaryKey (autoGenerate = false) val id: String,
    @ColumnInfo(name="question") var question:String?,
    //JSON String since Room does not support list of strings
    @ColumnInfo(name="inAnswers") var inAnswers:String?,
    @ColumnInfo(name="correctAnswer") var correctAnswer:String?,
    @ColumnInfo(name="category") var category: String?,
    @ColumnInfo(name="tags") var tags: String?,
    @ColumnInfo(name="difficulty") var difficulty: String?
    //isNiche, type
)