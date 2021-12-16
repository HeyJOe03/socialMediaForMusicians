package com.example.socialmedia.ProfileActivities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import com.example.socialmedia.loginsigninActivities.LogInActivity
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONArray


class ProfileActivity : AppCompatActivity(), ProfilePostRecycleView.OnItemClickListener {

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

    private val posts: MutableList<Post> = mutableListOf()

    private val gson : Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(b.root)

        sharedPref = this.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
        userID = sharedPref.getLong(GLOBALS.SP_KEY_ID,-1)

        layoutMenager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        adapter = ProfilePostRecycleView(emptyList(),this)

        profileRequest()

        b.myPostRV.adapter = adapter
        b.myPostRV.layoutManager = layoutMenager

        postRequests()
        //postRequests()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logOutMenu -> {
                logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logOut(){
        sharedPref.edit().remove(GLOBALS.SP_KEY_ID).apply()
        startActivity(Intent(this, LogInActivity::class.java))
        this.finish()
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

                for(i in 0 until (response["result"] as JSONArray).length()) {
                    val post = gson.fromJson((response["result"] as JSONArray).get(i).toString(),Post::class.java)
                    posts.add(post)
                    //Log.println(Log.DEBUG,"id",post.id.toString())
                }

                adapter.setData(posts.toList())

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

    private fun setProfileDataView(){
        b.descriptionTV.text = description
        b.instrumentInterestedInTV.text = instrumentInterestedIn
        b.usernameTV.text = username
    }


    override fun onClickListener(position: Int) {
        // Toast.makeText(this, posts[position].description, Toast.LENGTH_SHORT).show()
        val dialog = PostDialog(posts[position])
        dialog.show(supportFragmentManager,"post dialog")

        //dismiss()
    }

}