package com.example.socialmedia.ProfileActivities

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.dataClass.Post
import com.example.socialmedia.databinding.ActivityProfileBinding
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.lang.reflect.Type


class ProfileActivity : AppCompatActivity() {

    private lateinit var b: ActivityProfileBinding
    private lateinit var sharedPref: SharedPreferences
    private var userID: Long = -1

    private lateinit var username: String
    private lateinit var description: String
    private lateinit var instrumentInterestedIn: String
    //private lateinit var profileImage: Bitmap
    private var isLookingSomeoneToPlayWith: Boolean = false
    private lateinit var name: String
    private lateinit var email: String

    private lateinit var adapter: ProfilePostRecycleView
    private lateinit var layoutMenager: LinearLayoutManager

    private val gson : Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(b.root)

        val s = "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"
        val bit = try {
            val imageBytes = Base64.decode(s, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } catch (e: Exception) {
            e.message
            null
        }

        sharedPref = this.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
        userID = sharedPref.getLong(GLOBALS.SP_KEY_ID,-1)

        val titles = listOf("primo","secondo","terzo")

        layoutMenager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        adapter = ProfilePostRecycleView(titles)

        profileRequest()

        b.myPostRV.adapter = adapter
        b.myPostRV.layoutManager = layoutMenager

        postRequests()
        //postRequests()
    }

    private fun postRequests() {
        val postUrl = GLOBALS.SERVER_PROFILE_POSTS
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val postData = JSONObject()
        try {
            postData.put("id", userID)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            { response ->

                val groupListType: Type = object : TypeToken<ArrayList<Post>>() {}.type

                for(i in 0..((response["result"] as JSONArray).length()-1)) {
                    val b = gson.fromJson((response["result"] as JSONArray).get(i).toString(),Post::class.java)
                    Log.println(Log.DEBUG,"id",b.id.toString())
                }

                //val posts = gson.fromJson(response["result"].toString(),Post::class.java)


            }
        ) { error ->
            error.printStackTrace()
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun profileRequest(){
        val postUrl = GLOBALS.SERVER_PROFILE
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val postData = JSONObject()
        try {
            postData.put("id", userID)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            { response ->
                Log.println(Log.DEBUG,"response",response.toString())

                username = response["username"] as String
                name = response["name"] as String
                email = response["email"] as String
                description = response["description"] as String
                instrumentInterestedIn = response["instrument_interested_in"] as String
                val n = (response["is_looking_someone_to_play_with"] as Int)
                isLookingSomeoneToPlayWith = n == 1

                val url = GLOBALS.SERVER_PROFILE_PIC(userID)
                Log.println(Log.DEBUG,"url",url)

                b.profilePicture.load(url){
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder)
                    transformations(CircleCropTransformation())
                }

                setProfileDataView()
            }
        ) { error ->
            error.printStackTrace()
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun String.toBitmap(): Bitmap?{
        Base64.decode(this, Base64.DEFAULT).apply {
            return BitmapFactory.decodeByteArray(this,0,size)
        }
    }

    private fun setProfileDataView(){
        b.descriptionTV.text = description
        b.instrumentInterestedInTV.text = instrumentInterestedIn
        b.usernameTV.text = username
    }

}