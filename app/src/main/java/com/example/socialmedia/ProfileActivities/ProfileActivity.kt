package com.example.socialmedia.ProfileActivities

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
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
import com.example.socialmedia.databinding.ActivityProfileBinding
import org.json.JSONException
import org.json.JSONObject

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(b.root)

        sharedPref = this.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
        userID = sharedPref.getLong(GLOBALS.SP_KEY_ID,-1)

        val titles = listOf("primo","secondo","terzo")

        layoutMenager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        adapter = ProfilePostRecycleView(titles)

        profileRequest()

        b.myPostRV.adapter = adapter
        b.myPostRV.layoutManager = layoutMenager

        // TODO: postRequests()
        //postRequests()
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