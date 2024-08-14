package com.example.trivia_vivia

//API libs
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    private var API_URL = "https://the-trivia-api.com/v2/questions"
    //private var API_URL_IMG = "https://the-trivia-api.com/v2/questions?types=image_choice"
    private lateinit var questionDao: QuestionDao
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize the Room database and DAO
        questionDao = AppDatabase.getInstance(this).questionDao()
        // Fetch the question
        lifecycleScope.launch(Dispatchers.IO) {
            //questionDao.deleteAll()
            fetchQuestions()
        }
        // Initialize the bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        val fragmentManager: FragmentManager = supportFragmentManager
        val questionFragment: Fragment = QuestionFragment()
        val settingFragment: Fragment = SettingFragment()
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.question -> fragment = questionFragment
                R.id.settings -> fragment = settingFragment
            }
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit()
            true
        }
        bottomNavigationView.selectedItemId = R.id.question
    }

    private fun fetchQuestions() {
        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, API_URL, null,
            { response ->
                Log.d("API", "Fetched successfully")
                Toast.makeText(this, "Data fetched from API", Toast.LENGTH_SHORT).show()

                val questions = mutableListOf<Question>()

                for (i in 0 until response.length()) {
                    try {
                        val questionObj = response.getJSONObject(i)
                        val id = questionObj.getString("id")
                        val questionText = questionObj.getJSONObject("question").getString("text")
                        val correctAnswer = questionObj.getString("correctAnswer")
                        val incorrectAnswers = questionObj.getJSONArray("incorrectAnswers").toString()
                        val difficulty = questionObj.getString("difficulty")
                        val type = questionObj.getString("type")
                        val category = questionObj.getString("category")
                        val tags = questionObj.getJSONArray("tags").toString()


                        questions.add(
                            Question(
                                id, category, correctAnswer, incorrectAnswers,
                                questionText, tags, type, difficulty
                            )
                        )
                    } catch (e: JSONException) {
                        Log.e("API", "Error parsing question: ${e.message}")
                    }
                }

                // Convert questions to QuestionEntity objects
                val questionEntities = questions.map { quest ->
                    QuestionEntity(
                        id = quest.id,
                        question = quest.question,
                        inAnswers = quest.inAnswers,
                        correctAnswer = quest.correctAnswer,
                        category = quest.category,
                        tags = quest.tags,
                        difficulty = quest.difficulty
                    )
                }

                // Insert questions into the database and start the fragment
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        for (i in 0 until questionEntities.size) {
                            questionDao.insertQuestion(questionEntities[i])
                            Log.d("Database", "Added successfully " + i.toString())
                        }

                    } catch (e: Exception) {
                        Log.e("Database", "Error inserting questions: ${e.message}")
                    }
                }
            },
            { error ->
                Toast.makeText(this, "Failed to get response: $error", Toast.LENGTH_SHORT).show()
                Log.e("API", "Failed to get response: $error")
            }
        )

        queue.add(request)
    }


}
