package com.example.trivia_vivia

//API libs
import android.R.attr.name
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.serialization.decodeFromString
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private var API_URL = "https://the-trivia-api.com/v2/question/622a1c347cc59eab6f94f906"
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragment: Fragment = QuestionFragment()

        //Fetching the data from the API
        val queue = Volley.newRequestQueue(this@MainActivity)
        val request: StringRequest = object : StringRequest(
            Method.GET, API_URL,
            Response.Listener<String?> { response ->

                //loadingPB.setVisibility(View.GONE)
                //nameEdt.setText("")
                //jobEdt.setText("")

                Log.d("API","Fetched succesfully")
                Toast.makeText(this@MainActivity, "Data fetched from API", Toast.LENGTH_SHORT).show()
                try {
                    // parsing the response to json object to extract data from it.
                    val respObj = JSONObject(response)

                    //val name = respObj.getString("name")
                    //val job = respObj.getString("job")


                    // on below line we are setting this string s to our text view.
                    //responseTV.setText("Name : $name\nJob : $job")
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.e("API","Not able to fetch data")
                }
            },
            Response.ErrorListener { error -> // method to handle errors.
                Toast.makeText(
                    this@MainActivity,
                    "Fail to get response = $error",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            override fun getParams(): Map<String, String>? {
                // below line we are creating a map for
                // storing our values in key and value pair.
                val params: MutableMap<String, String> = HashMap()


                // on below line we are passing our key
                // and value pair to our parameters.
                //params["name"] = name
                //params["job"] = job


                // at last we are
                // returning our params.
                return params
            }
        }
        queue.add(request)
    }

}