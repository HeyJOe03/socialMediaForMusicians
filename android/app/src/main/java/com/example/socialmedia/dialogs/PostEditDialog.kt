package com.example.socialmedia.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
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
import com.example.socialmedia.databinding.PostEditDialogBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class PostEditDialog(
    private val post: Post
): DialogFragment() {
    private var mView: View? = null
    private lateinit var b: PostEditDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run{
            //initiate the binding here and pass the root to the dialog view
            b = PostEditDialogBinding.inflate(layoutInflater).apply {
                //reference layout elements by name freely here
            }
            AlertDialog.Builder(this).apply {
                setView(b.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.println(Log.ERROR,"post in long click",post.toString())

        (b.authorET as TextView).text = post.author
        (b.titleET as TextView).text = post.title
        (b.descriptionET as TextView).text = post.description

        b.btnEdit.setOnClickListener {
            updatePostRequest()
        }

        b.btnDelete.setOnClickListener{
            deletePostRequest()
        }

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun deletePostRequest() {

        val postUrl = GLOBALS.SERVER + "/post/delete"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val postData = JSONObject()
        postData.put("id", post.id)

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

        val postUrl = GLOBALS.SERVER + "/post/update"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val postData = JSONObject()
        postData.put("id", post.id)
        postData.put("author",b.authorET.text.toString())
        postData.put("title",b.authorET.text.toString())
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
}