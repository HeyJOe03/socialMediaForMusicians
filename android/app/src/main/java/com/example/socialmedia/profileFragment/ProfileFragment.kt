package com.example.socialmedia.profileFragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.socialmedia.ContentRecycleView
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.databinding.FragmentProfileBinding
import com.example.socialmedia.dialogs.*
import com.example.socialmedia.dialogs.edit.PostEditDialog
import com.example.socialmedia.dialogs.edit.SheetEditDialog
import com.example.socialmedia.dialogs.edit.ShopEditDialog
import com.example.socialmedia.dialogs.info.PostDialog
import com.example.socialmedia.dialogs.info.SheetDialog
import com.example.socialmedia.dialogs.info.ShopDialog
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONArray

class ProfileFragment(
    private val id: Long
) :
    Fragment(),
    ContentRecycleView.OnRVItemClickListener,
    PostEditDialog.SetOnDismiss, SheetEditDialog.SetOnDismiss, ShopEditDialog.SetOnDismiss,
    ProfileEditDialog.SetOnEditDialogClose {

    private var _binding: FragmentProfileBinding? = null
    private val b get() = _binding!!

    private var sharedPref: SharedPreferences? = null
    private var myUserID: Long = -1

    private lateinit var username: String
    private lateinit var description: String
    private lateinit var instrumentInterestedIn: String
    //private lateinit var profileImage: Bitmap
    private var isLookingSomeoneToPlayWith: Boolean = false
    private lateinit var name: String
    private lateinit var email: String
    //private var profileImg: Bitmap? = null

    private lateinit var adapterPost: ContentRecycleView
    private lateinit var adapterSheet: ContentRecycleView
    private lateinit var adapterInstrument: ContentRecycleView
    private var alreadyFollow: Boolean = false

    private lateinit var layoutMenager: LinearLayoutManager

    private val previewID: MutableList<Long> = mutableListOf()

    private var currentRoute = "post" // || "shop" || "sheet"

    //private var requestsDone = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        sharedPref = activity?.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
        myUserID = sharedPref!!.getLong(GLOBALS.SP_KEY_ID,-1)
        //setContentView(b.root)

        if(id == (-1).toLong() || myUserID == id){
            val hash_password = sharedPref!!.getString(GLOBALS.SP_KEY_PW,"")

            b.btnEditProfile.visibility = View.VISIBLE
            b.btnEditProfile.isClickable = true
            b.btnEditProfile.setOnClickListener{
                val dialog = ProfileEditDialog(this)
                dialog.show(parentFragmentManager, "edit profile")
            }

            b.btnFollow.visibility = View.GONE
            b.btnFollow.isClickable = false
        }
        else {
            myUserID = id
            b.btnEditProfile.visibility = View.GONE
            b.btnEditProfile.isClickable = false

            sharedPref = activity?.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
            val me = sharedPref!!.getLong(GLOBALS.SP_KEY_ID,-1)
            requestIfAlreadyFollowThisUser(me,myUserID)

            b.btnFollow.visibility = View.VISIBLE
            b.btnFollow.isClickable = true
            b.btnFollow.setOnClickListener {
                sharedPref = activity?.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
                val me = sharedPref!!.getLong(GLOBALS.SP_KEY_ID,-1)

                if(alreadyFollow) unFollowPostRequest(me,myUserID)
                else followPostRequest(me,myUserID)
            }
        }

        countFollowedAndFollower(myUserID)

        layoutMenager = GridLayoutManager(context,3)

        adapterPost = ContentRecycleView(emptyList(),GLOBALS.CONTENT_POST,this)
        adapterSheet = ContentRecycleView(emptyList(), GLOBALS.CONTENT_SHEET,this)
        adapterInstrument = ContentRecycleView(emptyList(), GLOBALS.CONTENT_SHOP,this)

        b.previewRV.adapter = adapterPost //DEFAULT ADAPTER IS ON POST
        b.previewRV.layoutManager = layoutMenager

        b.refreshLayout.setOnRefreshListener {
            Handler(Looper.myLooper()!!).postDelayed(
                { // Runnable
                    refresh()
                    b.refreshLayout.isRefreshing = false
                },
                1000,
            ) // This is how you can choose when it will end
        }

        b.btnPostRV.setOnClickListener{
            b.previewRV.adapter = adapterPost
            currentRoute = GLOBALS.CONTENT_POST
            idRequest(GLOBALS.CONTENT_POST)
        }

        b.btnSheetRV.setOnClickListener{
            b.previewRV.adapter = adapterSheet
            currentRoute = GLOBALS.CONTENT_SHEET
            idRequest(GLOBALS.CONTENT_SHEET)
        }

        b.btnShopRV.setOnClickListener{
            currentRoute = GLOBALS.CONTENT_SHOP
            b.previewRV.adapter = adapterInstrument
            idRequest(GLOBALS.CONTENT_SHOP)
        }

        refresh()
    }

    private fun idRequest(route: String) {

        val postUrl = GLOBALS.GET_CONTENT_IDS(route)
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val postData = JSONObject()
        try {
            postData.put("id", myUserID)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            { response ->
                previewID.clear()

                for(i in 0 until (response["result"] as JSONArray).length()) {
                    val id = (((response["result"] as JSONArray).get(i) as JSONObject).getLong("id"))
                    //val post = gson.fromJson((response["result"] as JSONArray).get(i).toString(),Post::class.java)
                    previewID.add(id)
                }
                when(route){
                    GLOBALS.CONTENT_POST -> adapterPost.setData(previewID.toList())
                    GLOBALS.CONTENT_SHEET -> adapterSheet.setData(previewID.toList())
                    GLOBALS.CONTENT_SHOP -> adapterInstrument.setData(previewID.toList())
                }
            }
        ) { error ->
            error.printStackTrace()
            Log.println(Log.ERROR,"error","error")
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun followPostRequest(follower: Long, followed: Long) {

        val postUrl = GLOBALS.FOLLOW_ROUTE
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val postData = JSONObject()
        try {
            postData.put("follower", follower)
            postData.put("followed",followed)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            {
                alreadyFollow = true
                b.btnFollow.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey_500))
            }
        ) {}

        requestQueue.add(jsonObjectRequest)
    }

    private fun unFollowPostRequest(follower: Long, followed: Long){
        val postUrl = GLOBALS.UNFOLLOW_ROUTE
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val postData = JSONObject()
        try {
            postData.put("follower", follower)
            postData.put("followed",followed)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            {
                alreadyFollow = false
                b.btnFollow.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey_700))
            }
        ) {}

        requestQueue.add(jsonObjectRequest)
    }

    private fun countFollowedAndFollower(us: Long){
        val postUrl = GLOBALS.COUNT_FOLLOW
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val postData = JSONObject()
        try {
            postData.put("id", us)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            {
                b.followedTV.text = it["followed"].toString()
                b.followerTV.text = it["follower"].toString()
            }
        ) {
            Log.println(Log.ERROR,"error",String(it.networkResponse.data))
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun profileRequest(){
        val postUrl = GLOBALS.SERVER_PROFILE
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val postData = JSONObject()
        try {
            postData.put("id", myUserID)
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

                b.profilePicture.load(GLOBALS.SERVER_PROFILE_PIC(myUserID)){
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder)
                    memoryCachePolicy(CachePolicy.DISABLED) //without this don't update from the same url
                    transformations(CircleCropTransformation())
                }

                setProfileDataView()
            }
        ) { error ->
            error.printStackTrace()
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun requestIfAlreadyFollowThisUser(me: Long, other: Long){
        val postUrl = GLOBALS.FOLLOW_CHECK_ROUTE
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val postData = JSONObject()
        try {
            postData.put("follower", me)
            postData.put("followed",other)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            {
                alreadyFollow = it["alreadyFollow"] as Boolean
                if(alreadyFollow) b.btnFollow.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey_500))
                else b.btnFollow.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey_700))
            }
        ) {}

        requestQueue.add(jsonObjectRequest)
    }

    private fun setProfileDataView(){
        b.descriptionTV.text = description
        b.instrumentInterestedInTV.text = instrumentInterestedIn
        b.usernameTV.text = username
    }

    override fun onRVClickListener(position: Int, typeOfRV: String) {
        val dialog: DialogFragment? = when(typeOfRV){
            GLOBALS.CONTENT_POST -> PostDialog(previewID[position])
            GLOBALS.CONTENT_SHEET -> SheetDialog(previewID[position])
            GLOBALS.CONTENT_SHOP -> ShopDialog(previewID[position])
            else -> null
        }
        dialog?.show(childFragmentManager,"post dialog")
    }

    override fun onRVLongClickListener(position: Int, typeOfRV: String) {

        sharedPref = activity?.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
        val me = sharedPref!!.getLong(GLOBALS.SP_KEY_ID,-1)

        if(id == (-1).toLong() || id != me) return

        val dialog: DialogFragment? = when(typeOfRV){
            GLOBALS.CONTENT_POST -> PostEditDialog(previewID[position],this)
            GLOBALS.CONTENT_SHEET -> SheetEditDialog(previewID[position],this)
            GLOBALS.CONTENT_SHOP -> ShopEditDialog(previewID[position],this)
            else -> null
        }
        dialog?.show(parentFragmentManager,"post update-delete dialog")
    }

    override fun onEditDialogClose() {
        refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun refresh(){
        idRequest(currentRoute)
        countFollowedAndFollower(myUserID)
        profileRequest()
    }

    override fun onDismiss() {
        refresh()
    }

}