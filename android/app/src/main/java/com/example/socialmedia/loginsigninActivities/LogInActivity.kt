package com.example.socialmedia.loginsigninActivities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.MainActivity
import com.example.socialmedia.R
import com.example.socialmedia.databinding.ActivityLogInBinding
import com.example.socialmedia.objects.HashSHA256

import org.json.JSONObject

import org.json.JSONException

class LogInActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            if(binding.usernameET.text.toString() != "" &&  binding.passwordET.text.toString() != "") signInRequest()
            else inputError()
        }

        binding.gotoRegisterBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun signInRequest(){
        val postUrl = GLOBALS.SERVER_LOG_IN
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val postData = JSONObject()
        try {
            postData.put("username", binding.usernameET.text.toString())
            postData.put("pw", HashSHA256.hash(binding.passwordET.text.toString()).substring(0,32)) //takes only first 32 character
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            { response ->
                Log.println(Log.DEBUG,"response",response[GLOBALS.KEY_SIGNIN].toString())

                if(response[GLOBALS.KEY_SIGNIN].toString() != GLOBALS.SIGN_IN_FAILED){
                    val ID: Long =
                        try {
                            response[GLOBALS.KEY_SIGNIN].toString().toLong()
                        }catch( e : NumberFormatException){
                            -1
                        }

                    val sharedPreferences : SharedPreferences = this.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER,Context.MODE_PRIVATE)

                    sharedPreferences.edit().putLong(GLOBALS.SP_KEY_ID, ID).apply()
                    sharedPreferences.edit().putString(GLOBALS.SP_KEY_PW, postData.getString("pw")).apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    this.finish()
                }
                else inputError()
            }
        ) { error -> error.printStackTrace() }

        requestQueue.add(jsonObjectRequest)
    }

    private fun inputError(){
        Toast.makeText(this,"Username or password not valid",Toast.LENGTH_LONG).show()
        binding.usernameET.text.clear()
        binding.usernameET.setBackgroundResource(R.drawable.edit_text_design_error)
        binding.passwordET.text.clear()
        binding.passwordET.setBackgroundResource(R.drawable.edit_text_design_error)
    }
}