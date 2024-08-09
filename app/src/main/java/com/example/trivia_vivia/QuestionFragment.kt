package com.example.trivia_vivia

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class QuestionFragment: Fragment() {
    //database operation
    private lateinit var questionDao: QuestionDao
    //elements
    private lateinit var view: View
    var answerButtons = mutableListOf<Button>()
    private lateinit var nextButton: Button
    //logic check
    private lateinit var correct: String
    var correctIndex:Int = 0

    //List of answer buttons
    val answerViews = listOf(
        R.id.questView_answer_1,
        R.id.questView_answer_2,
        R.id.questView_answer_3,
        R.id.questView_answer_4
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.quiz_view, container, false)
        val context = view.context
        //val layoutManager = LinearLayoutManager(context)
        //add buttons to the answer button list
        (0 until answerViews.size).forEach { i ->
            answerButtons.add(view.findViewById<Button>(answerViews[i]))
        }
        nextButton = view.findViewById(R.id.next_button)
        questionDao = AppDatabase.getInstance(context).questionDao()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionAdapter()
        nextButton.setOnClickListener {
            questionAdapter()
        }


    }

    private fun questionAdapter(){
        answerButtons.forEach { button ->
            button.setBackgroundResource(R.drawable.button_answer)
        }
        fetchAndDisplayQuestion()
        //listeners for buttons

        answerButtons.forEachIndexed { index, button ->
            button.setOnClickListener{
                nextButton.isClickable = true
                //disable buttons
                answerButtons.forEach { button ->
                    button.setOnClickListener(null)
                }
                val clickedAnswer = button.text.toString()
                val isCorrect = checkAnswer(clickedAnswer)
                answerButtons[correctIndex].setBackgroundResource(R.drawable.correct_button_answer)
                //incorrect answer
                if (!isCorrect){
                    answerButtons[index].setBackgroundResource(R.drawable.incorrect_button_answer)
                }
            }
        }
    }

    private fun fetchAndDisplayQuestion() {
        lifecycleScope.launch {
            try {
                val questionCount = withContext(Dispatchers.IO) {
                    questionDao.getCount()
                }
                Log.d("QuestionFragment", "Total questions in database: $questionCount")

                val randomQuestion = withContext(Dispatchers.IO) {
                    questionDao.getRandomQuestion()
                }

                if (randomQuestion != null) {
                    displayQuestion(randomQuestion)
                } else {
                    Log.d("QuestionFragment", "No questions available in the database")
                }
            } catch (e: Exception) {
                Log.e("QuestionFragment", "Error fetching random question: ${e.message}")
                Toast.makeText(context, "Error loading question", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //function that displays a question (DONE)
    private fun displayQuestion(questionEntity: QuestionEntity){
        val converter = Converter()

        val incorrectAnswers = converter.fromString(questionEntity.inAnswers)
        val correctAnswer = questionEntity.correctAnswer.toString()
        correct = correctAnswer
        //List of answers
        var answers:MutableList<String> = mutableListOf()
        if (incorrectAnswers != null) {
            (0 until incorrectAnswers.size).forEach { i ->
                // Add the incorrect answers to a list of answers
                answers.add((incorrectAnswers[i]))
            }
        }
        //Add the correct one
        answers.add(correctAnswer)
        //Shuffle the list
        answers.shuffle()

        //Log.d("Binding",view.toString())
        //Paste the question
            view.findViewById<TextView?>(R.id.questView_question)?.text = questionEntity.question
        //A list in Kotlin using size instead of length
        (0 until answers.size).forEach { i ->
            view.findViewById<TextView>(answerViews[i])?.text = answers[i]
            if (answers[i] == correct){
                correctIndex = i
            }
        }
    }

    private fun checkAnswer(clickedAnswer: String): Boolean {
        val isCorrect = clickedAnswer == correct
        if (isCorrect) {
            Toast.makeText(context, "Correct Answer!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Incorrect! The correct answer is $correct", Toast.LENGTH_SHORT).show()
        }
        return isCorrect
    }

}