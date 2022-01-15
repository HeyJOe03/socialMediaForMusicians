package com.example.socialmedia.dialogs.edit

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.dataClass.Post
import com.example.socialmedia.databinding.DialogPostEditBinding
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class PostEditDialog(
    private val id: Long,
    private val setOnDismiss: SetOnDismiss
): DialogFragment() {
    private var mView: View? = null
    private lateinit var b: DialogPostEditBinding
    private val gson: Gson = Gson()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run{
            //initiate the binding here and pass the root to the dialog view
            b = DialogPostEditBinding.inflate(layoutInflater).apply {
                //reference layout elements by name freely here
            }
            AlertDialog.Builder(this).apply {
                setView(b.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setTheViewWithData(Post(-1,"",null,null,null,-1,"",""))
        postRequest()

        b.btnEdit.setOnClickListener {
            updatePostRequest()
        }

        b.btnDelete.setOnClickListener{
            deletePostRequest()
        }

        b.postEditDialogLayout.setOnClickListener {
            dismiss()
        }

        return mView
    }

    private fun deletePostRequest() {

        val postUrl = GLOBALS.DELETE_DATA(GLOBALS.CONTENT_POST)
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val postData = JSONObject()
        postData.put("id",id)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            { dismiss() }
        ) { error ->
            error.printStackTrace()
            dismiss()
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun updatePostRequest() {

        val postUrl = GLOBALS.UPDATE_DATA(GLOBALS.CONTENT_POST)
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val postData = JSONObject()
        postData.put("id", id)
        postData.put("author",b.authorET.text.toString())
        postData.put("title",b.titleET.text.toString())
        postData.put("description",b.descriptionET.text.toString())

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            {response -> dismiss() }
        ) { error ->
            error.printStackTrace()
            dismiss()
        }

        requestQueue.add(jsonObjectRequest)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        setOnDismiss.onDismiss()
    }

    private fun setTheViewWithData(post: Post){
        (b.authorET as TextView).text = post.author
        (b.titleET as TextView).text = post.title
        (b.descriptionET as TextView).text = post.description
    }

    private fun postRequest() {

        val postUrl = GLOBALS.INFO_DATA(GLOBALS.CONTENT_POST)
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

    interface SetOnDismiss{
        fun onDismiss()
    }
}