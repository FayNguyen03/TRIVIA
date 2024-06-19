package com.example.trivia_vivia

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationBarView
import com.
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.Header
import kotlinx.serialization.decodeFromString
import org.apache.http.Header;
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    private var API_URL = "https://the-trivia-api.com/v2/questions"
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragment: Fragment = QuestionFragment()

        //Fetching the data from the API
        val client = AsyncHttpClient()
        client.get(API_URL, object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch recipe: $recipeId")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched recipe: $json")
                try {
                    val jsonObject = json.jsonObject.toString()
                    val recipeDetail = createJson().decodeFromString<RecipeDetail>(jsonObject)
                    val extendedIngredients = recipeDetail.extendedIngredients
                    if (extendedIngredients != null) {
                        for (ingredient in extendedIngredients) {
                            ingredients.add(ingredient)
                        }
                        Log.d("Ingredients",ingredients.size.toString())
                    }
                    ingredientAdapter.notifyDataSetChanged()
                    Log.i(TAG, recipeDetail.toString())

                    Glide.with(context)
                        .load(recipeDetail.image)
                        .into(recipeImageView)
                    recipeTitleTextView.text = recipeDetail.title
                    recipeDescriptionTextView.text = "Total Cooking Time:" + recipeDetail.cookingMinute.toString() + " minutes"



                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }
        })
    }



}