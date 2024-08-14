package com.example.trivia_vivia

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImgQuestionDao {
    @Query("SELECT * FROM img_question_table")
    fun getAllImgQuestions(): Flow<List<ImgQuestionEntity>>

    //Randomly choose a row in table
    @Query("SELECT * FROM img_question_table ORDER BY RANDOM() LIMIT 1;")
    fun getRandomImgQuestion(): ImgQuestionEntity

    //Randomly choose an easy row in table
    @Query("SELECT * FROM img_question_table WHERE difficulty = 'easy' ORDER BY RANDOM() LIMIT 1;")
    fun getRandomImgQuestionEasy(): ImgQuestionEntity

    //Randomly choose a medium row in table
    @Query("SELECT * FROM img_question_table WHERE difficulty = 'medium' ORDER BY RANDOM() LIMIT 1;")
    fun getRandomImgQuestionMedium(): ImgQuestionEntity

    //Randomly choose a hard row in table
    @Query("SELECT * FROM img_question_table WHERE difficulty = 'hard' ORDER BY RANDOM() LIMIT 1;")
    fun getRandomImgQuestionHard(): ImgQuestionEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImgQuestion(question: ImgQuestionEntity)

    @Query("DELETE FROM img_question_table")
    fun deleteAllImg()

    @Query("SELECT COUNT(*) FROM img_question_table")
    fun getCountImg(): Int
}