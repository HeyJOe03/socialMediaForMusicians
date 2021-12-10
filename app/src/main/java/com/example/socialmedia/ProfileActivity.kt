package com.example.socialmedia

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
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
    private lateinit var profileImage: Bitmap
    private var isLookingSomeoneToPlayWith: Boolean = false
    private lateinit var name: String
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(b.root)

        sharedPref = this.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
        userID = sharedPref.getLong(GLOBALS.SP_KEY_ID,-1)

        profileRequest()

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
                Log.println(Log.DEBUG,"response",(response["profile_image"] as String).length.toString())
                username = response["username"] as String
                name = response["name"] as String
                email = response["email"] as String
                val n = (response["is_looking_someone_to_play_with"] as Int)
                isLookingSomeoneToPlayWith = n == 1
                val base64profileImage = response["profile_image"] as String
                profileImage = decodePicString(base64profileImage)
                b.profilePicture.setImageBitmap(profileImage)

            }
        ) { error ->
            error.printStackTrace()
        }

        requestQueue.add(jsonObjectRequest)
    }

    private  fun decodePicString (encodedString: String): Bitmap {

        val imageBytes: ByteArray = Base64.decode(encodedString.substring(0,encodedString.indexOf(',') + 1), Base64.DEFAULT)
        Log.println(Log.DEBUG,"imagebytes size",imageBytes.size.toString())
        val decodedImage: Bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }
}