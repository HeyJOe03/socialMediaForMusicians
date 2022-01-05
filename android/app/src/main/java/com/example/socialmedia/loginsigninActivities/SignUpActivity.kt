package com.example.socialmedia.loginsigninActivities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.MainActivity
import com.example.socialmedia.R
import com.example.socialmedia.databinding.ActivitySignUpBinding
import com.example.socialmedia.dialogs.RegistrationErrorDialog
import com.example.socialmedia.objects.HashSHA256
import org.json.JSONException
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {

    private interface VolleyCallback {
        fun onSuccess(usernameIsAlreadyTaken: Boolean)
    }

    private lateinit var b: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnLookingForOtherPlayers.setOnClickListener {
            val bgState : Boolean = b.btnLookingForOtherPlayers.background.constantState == getDrawable(
                R.drawable.btn_looking_for_other_players
            )?.constantState

            if (bgState) {
                b.btnLookingForOtherPlayers.setBackgroundResource(R.drawable.btn_not_looking_for_other_players)
                b.btnLookingForOtherPlayers.text = "I'm not looking for other people to play with"
            }
            else {
                b.btnLookingForOtherPlayers.setBackgroundResource(R.drawable.btn_looking_for_other_players)
                b.btnLookingForOtherPlayers.text = "I'm looking for other people to play with"
            }
        }

        b.gotoRegisterBtn.setOnClickListener{
            startActivity(Intent(this, LogInActivity::class.java))
            this.finish()
        }

        b.btnSubmit.setOnClickListener {

            hideKeyboard()

            val name = b.nameET.text.toString()
            var pw = b.passwordET.text.toString()
            val email = b.emailET.text.toString()
            val username = b.usernameET.text.toString()
            val cpw = b.confirmPasswordET.text.toString()
            val lookingForOtherPlayers : Boolean = b.btnLookingForOtherPlayers.background.constantState == getDrawable(
                R.drawable.btn_looking_for_other_players
            )?.constantState


            val dataState = inputsCheck(name,pw,email,username,cpw)

            if (!dataState) Toast.makeText(this,"Error in the registration, check the password and the fields",Toast.LENGTH_LONG).show()

            checkIfUsernameIsAlreadyUsedRequest(username,
                object : VolleyCallback {
                    override fun onSuccess(usernameIsAlreadyTaken: Boolean) {
                        if(usernameIsAlreadyTaken) {
                            Toast.makeText(
                                applicationContext,"Invalid username, your user name is already taken",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else {
                            pw = HashSHA256.hash(pw).substring(0,32)
                            submitNewUserRequest(username,pw,email,name,lookingForOtherPlayers)
                        }
                    }
                })
        }
    }

    private fun submitNewUserRequest(username: String, password:String, email: String, name: String, lookingForOtherPlayers:Boolean){
        val postUrl = GLOBALS.SERVER_SIGN_UP
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val postData = JSONObject()
        try {
            postData.put("username", username)
            postData.put("hash_password",password)
            postData.put("email",email)
            postData.put("name",name)
            postData.put("hash_password",password)
            postData.put("is_looking_someone_to_play_with", lookingForOtherPlayers)
            Log.println(Log.DEBUG,"request:", postData.toString())

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            { response ->
                Log.println(Log.ERROR, "response", response["error"].toString())
                if(response["error"].toString() != "good"){
                    //Toast.makeText(applicationContext, response["error"].toString(), Toast.LENGTH_SHORT).show()
                    Log.println(Log.ERROR, "error", response["error"].toString())

                    openErrorFragment(response["error"].toString())
                }
                else{
                    val myID = response["insertID"].toString().toLong()
                    if(myID != (-1).toLong()){

                        val sharedPreferences : SharedPreferences = this.getSharedPreferences(
                            GLOBALS.SHARED_PREF_ID_USER,
                            Context.MODE_PRIVATE)

                        sharedPreferences.edit().putLong(GLOBALS.SP_KEY_ID, myID).apply()
                        sharedPreferences.edit().putString(GLOBALS.SP_KEY_PW, password).apply()

                        startActivity(Intent(this, MainActivity::class.java))
                        this.finish()
                    } else{
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        ) { error ->
            error.printStackTrace()
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun inputsCheck(name:String,pw:String,email:String,username:String,cpw:String) : Boolean{
        return when{ //more checks comes from the server
            username == "" -> false
            name == "" -> false
            pw == "" -> false
            email == "" -> false
            cpw != pw -> false
            else -> true
        }
    }

    private fun checkIfUsernameIsAlreadyUsedRequest(username:String, volleyCallback: VolleyCallback){
        val postUrl = GLOBALS.SERVER_USER_EXIST
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val postData = JSONObject()
        try {
            postData.put("username", username)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            { response ->
                //Log.println(Log.DEBUG,"response",response["exist"].toString())
                response["exist"].toString()
                volleyCallback.onSuccess(response["exist"].toString().toBoolean())
            }
        ) { error ->
            error.printStackTrace()
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun hideKeyboard(){
        // since our app extends AppCompatActivity, it has access to context
        val imm=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // we have to tell hide the keyboard from what. inorder to do is we have to pass window token
        // all of our views,like message, name, button have access to same window token. since u have button
        imm.hideSoftInputFromWindow(b.btnSubmit.windowToken, 0)

        // if you are using binding object
        // imm.hideSoftInputFromWindow(binding.button.windowToken,0)

    }

    private fun openErrorFragment(error : String){

        val dialog = RegistrationErrorDialog.newInstance(error)
        dialog.show(supportFragmentManager, "Filter Dialog")

    }
}