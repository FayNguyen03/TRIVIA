package com.example.trivia_vivia

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthDao {
    @Query("SELECT * FROM question_table")
    fun getAll(): Flow<List<QuestionEntity>>

    //Randomly choose a row in table
    @Query("SELECT * FROM question_table ORDER BY RANDOM() LIMIT 1;")
    fun getRandomQuestion(): QuestionEntity

    @Insert
    fun insert(entry: QuestionEntity)

    @Query("DELETE FROM question_table")
    fun deleteAll()
}
