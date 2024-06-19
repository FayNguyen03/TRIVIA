package com.example.trivia_vivia

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="question_table")
data class QuestionEntity (
    @PrimaryKey(autoGenerate = false) val id: String? = "",
    @ColumnInfo(name="question") var question:String?,
    @ColumnInfo(name="answers") var answers:List<String>?,
    @ColumnInfo(name="category") var category: String?,
    @ColumnInfo(name="tags") var tags: List<String>?,
    @ColumnInfo(name="difficulty") var difficulty: String?
    //isNiche, type
)