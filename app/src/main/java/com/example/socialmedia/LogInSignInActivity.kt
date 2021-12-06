package com.example.socialmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.socialmedia.databinding.ActivityLogInSignInBinding
import okhttp3.Response

import org.json.JSONObject

import org.json.JSONException




class LogInSignInActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLogInSignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnSubmit.setOnClickListener {
            //Toast.makeText(this,  HashSHA256.hash("ciaone"), Toast.LENGTH_SHORT).show()
            if(binding.usernameET.text.toString() != "" &&  binding.passwordET.text.toString() != "") signIn()
            else inputError()
        }
    }


    private fun signIn(){
        val postUrl = GLOBALS.SERVER + "login"
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val postData = JSONObject()
        try {
            postData.put("username", binding.usernameET.text.toString())
            postData.put("pw", HashSHA256.hash(binding.passwordET.text.toString()))
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            { response ->
                //Log.println(Log.DEBUG,"response",response.toString())

                if(response[GLOBALS.KEY_SIGNIN] == GLOBALS.SIGN_IN_GOOD){
                    //start main activity
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