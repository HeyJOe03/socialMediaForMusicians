package com.example.socialmedia.profileFragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import com.example.socialmedia.databinding.FragmentProfileBinding
import com.example.socialmedia.dialogs.PostDialog
import com.example.socialmedia.dialogs.PostEditDialog
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONArray


class ProfileFragment : Fragment(), ProfilePostRecycleView.OnItemClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val b  get() = _binding!!

    private var sharedPref: SharedPreferences? = null
    private var userID: Long = -1

    private lateinit var username: String
    private lateinit var description: String
    private lateinit var instrumentInterestedIn: String
    //private lateinit var profileImage: Bitmap
    private var isLookingSomeoneToPlayWith: Boolean = false
    private lateinit var name: String
    private lateinit var email: String
    //private var profileImg: Bitmap? = null

    private lateinit var adapter: ProfilePostRecycleView
    private lateinit var layoutMenager: LinearLayoutManager

    private val posts: MutableList<Post> = mutableListOf()

    private val gson : Gson = Gson()

    private var requestsDone = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        //setContentView(b.root)

        sharedPref = activity?.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
        userID = sharedPref!!.getLong(GLOBALS.SP_KEY_ID,-1)

        layoutMenager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        adapter = ProfilePostRecycleView(emptyList(),this)

        b.myPostRV.adapter = adapter
        b.myPostRV.layoutManager = layoutMenager

        if(!requestsDone){
            postRequests()
            profileRequest()
            requestsDone = true
        } else {
            adapter.setData(posts.toList())
            setProfileDataView()
        }
    }

    private fun postRequests() {

        val postUrl = GLOBALS.SERVER_PROFILE_POSTS
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

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
                //Log.println(Log.DEBUG,"lenght",(response["result"] as JSONArray).length().toString())
                for(i in 0 until (response["result"] as JSONArray).length()) {
                    val post = gson.fromJson((response["result"] as JSONArray).get(i).toString(),Post::class.java)
                    posts.add(post)
                }
                adapter.setData(posts.toList())
            }
        ) { error ->
            error.printStackTrace()
            Log.println(Log.ERROR,"error","errror")

        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun profileRequest(){
        val postUrl = GLOBALS.SERVER_PROFILE
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

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
                //profileImg = (response["profile_image"] as String).toBitmap()

                val url = GLOBALS.SERVER_PROFILE_PIC(userID)

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

        val dialog = PostDialog(posts[position])
        dialog.show(childFragmentManager,"post dialog")

        //dismiss()
    }

    override fun onLongClickListener(position: Int) {

        val dialog = PostEditDialog(posts[position])
        dialog.show(childFragmentManager,"post update-delete dialog")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun String.toBitmap(): Bitmap? {
        Base64.decode(this, Base64.DEFAULT).apply {
            return BitmapFactory.decodeByteArray(this, 0, size)
        }
    }

}