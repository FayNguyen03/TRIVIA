package com.example.trivia_vivia

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
//Data Access Object (DAO): Defines methods for interacting with the database.
@Dao
interface QuestionDao {
    @Query("SELECT * FROM question_table")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    //Randomly choose a row in table
    @Query("SELECT * FROM question_table ORDER BY RANDOM() LIMIT 1;")
    fun getRandomQuestion(): QuestionEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(question: QuestionEntity)

    @Query("DELETE FROM question_table")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM question_table")
    fun getCount(): Int
}
