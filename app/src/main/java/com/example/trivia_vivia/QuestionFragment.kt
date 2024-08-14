package com.example.trivia_vivia

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import kotlin.random.Random

class QuestionFragment: Fragment() {
    //database operation
    private lateinit var questionDao: QuestionDao
    //elements
    private lateinit var view: View
    var answerButtons = mutableListOf<Button>()
    private lateinit var nextButton: Button
    //logic check
    private lateinit var correct: String
    var correctIndex: Int = 0
    //question tag
    private lateinit var chipGroupTags: ChipGroup
    private var difficulty: Int = 0
    private var answerType: String = "text"
    private lateinit var level: String
    private lateinit var scoreCard: CardView
    private val sharedViewModel: SharedViewModel by activityViewModels()
    //private lateinit var questionTag: List<String>
    var SCORE: Int = 0
    //List of answer buttons
    val answerViews = listOf(
        R.id.questView_answer_1,
        R.id.questView_answer_2,
        R.id.questView_answer_3,
        R.id.questView_answer_4
    )
    //list of difficulty score
    val difficultyMap = hashMapOf("hard" to 5, "medium" to 3, "easy" to 1)
    //List of chip colors
    val colorTags = listOf(
        R.color.orchid_pink,
        R.color.columbia_blue
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
        chipGroupTags = view.findViewById(R.id.tag_chip_group)
        scoreCard = view.findViewById(R.id.score_card)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        answerType = sharedViewModel.selectedAnswerType
        difficulty = sharedViewModel.selectedDifficulty
        questionAdapter()
        nextButton.setOnClickListener {
            questionAdapter()
        }
    }

    private fun questionAdapter(){
        answerButtons.forEach { button ->
            button.setBackgroundResource(R.drawable.button_answer)
            nextButton.text = "Skip"
        }
        fetchAndDisplayQuestion()

        //listeners for buttons

        answerButtons.forEachIndexed { index, button ->
            button.setOnClickListener{
                nextButton.text = "Next"
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
                else{
                    SCORE += difficultyMap[level.lowercase()]!!
                    view.findViewById<TextView>(R.id.current_score_text)?.text = SCORE.toString()

                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun fetchAndDisplayQuestion(){

        lifecycleScope.launch {
            try {
                val questionCount = withContext(Dispatchers.IO) {
                    questionDao.getCount()
                }
                Log.d("QuestionFragment", "Total questions in database: $questionCount")

                val randomQuestion = withContext(Dispatchers.IO) {
                    if (difficulty == 3){
                        questionDao.getRandomQuestion()
                    }
                    else if(difficulty == 0){
                        questionDao.getRandomQuestionEasy()
                    }
                    else if(difficulty == 1){
                        questionDao.getRandomQuestionMedium()
                    }
                    else{
                        questionDao.getRandomQuestionHard()
                    }
                }

                if (randomQuestion != null) {
                    displayQuestion(randomQuestion)
                    //questionTag = converter.fromString(randomQuestion.tags) ?.toList()!!
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
        level = questionEntity.difficulty.toString()
        if (level == "easy" || level == ""){
            scoreCard.setCardBackgroundColor(getResources().getColor(R.color.antique_white))
        }
        else if (level == "medium"){
            scoreCard.setCardBackgroundColor(getResources().getColor(R.color.columbia_blue))
        }
        else if(level == "hard"){
            scoreCard.setCardBackgroundColor(getResources().getColor(R.color.red))
        }
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
        view.findViewById<TextView?>(R.id.questView_question)?.text = questionEntity.question
        //A list in Kotlin using size instead of length
        (0 until answers.size).forEach { i ->
            view.findViewById<TextView>(answerViews[i])?.text = answers[i]
            if (answers[i] == correct){
                correctIndex = i
                Log.d("Correct",i.toString())
            }
        }
        chipGroupTags.removeAllViews()
        Log.d("Chip", questionEntity.tags.toString())
        val questionTags = converter.fromString(questionEntity.tags)
        //chipGroupTags.removeAllViews() // Clear existing chips

        questionTags?.forEachIndexed { index, tag ->
            //Log.d("Chip", "Starting iteration for index $index")
            //Log.d("Chip", "Adding chip $index: $tag")
            context?.let { ctx ->
                Log.d("Chip", "Context is not null, creating chip")
                try {
                    Chip(ctx).apply {
                        text = tag
                        isClickable = false
                        isCheckable = false
                        setChipBackgroundColorResource(colorTags[index % 2])
                        //Log.d("Chip", "Chip created, adding to chipGroupTags")
                        chipGroupTags.addView(this)
                        //Log.d("Chip", "Chip added successfully")
                    }
                } catch (e: Exception) {
                    Log.e("Chip", "Exception while creating or adding chip: ${e.message}")
                }
            } ?: run {
                Log.e("Chip", "Context is null, can't create Chip for tag: $tag")
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