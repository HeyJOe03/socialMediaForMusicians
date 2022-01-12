package com.example.socialmedia.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import coil.load
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.dataClass.Post
import com.example.socialmedia.databinding.DialogPostBinding
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PostDialog(
    val id: Long
): DialogFragment() {

    private val gson: Gson = Gson()

    private var mView: View? = null

    private lateinit var b: DialogPostBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            //initiate the binding here and pass the root to the dialog view
            b = DialogPostBinding.inflate(layoutInflater).apply {
                //reference layout elements by name freely here
            }
            AlertDialog.Builder(this).apply {
                setView(b.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setTheViewWithData(Post(-1,"",null,null,null,-1,"",""))

        b.postDialogLayout.setOnClickListener{
            dismiss()
        }
        return mView
    }

    fun setTheViewWithData(post: Post){
        b.authorTV.text = post.author
        b.descriptionTV.text = post.description
        b.titleTV.text = post.title
        b.likesTV.text = post.likes.toString()
        //b.postImg.setImageBitmap(post.content.toBitmap())
        b.postImg.load(GLOBALS.POST_IMG(post.id)){
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
        }
    }

    private fun postRequests() {

        val postUrl = GLOBALS.SERVER + "/data/post/info"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val postData = JSONObject()
        try {
            postData.put("id", id)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            { response ->
                val s = (response as JSONObject).toString()
                val post = gson.fromJson(s,Post::class.java)
                setTheViewWithData(post)
            }
        ) { error ->
            error.printStackTrace()
            Log.println(Log.ERROR,"error","error")
        }

        requestQueue.add(jsonObjectRequest)
    }

}