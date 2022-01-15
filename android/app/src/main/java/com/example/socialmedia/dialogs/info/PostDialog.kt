package com.example.socialmedia.dialogs.info

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import coil.load
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.addPostFragment.PostPreview1Fragment
import com.example.socialmedia.dataClass.Post
import com.example.socialmedia.databinding.DialogPostBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class PostDialog(
    val id: Long
): DialogFragment() {

    private val gson: Gson = Gson()
    private var mView: View? = null
    private lateinit var layout: View
    private lateinit var b: DialogPostBinding
    private var hasWriteMemoryPermission: Boolean = false

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

        mView = b.root

        setTheViewWithData(Post(-1,"",null,null,null,-1,"",""))

        postRequest()

        b.postImg.setOnLongClickListener {
            if(hasWriteMemoryPermission) {
                val drawable: Drawable = b.postImg.drawable
                val bitmap: Bitmap = drawable.toBitmap()
                saveImage(bitmap)
            }
            else requestPermission(mView!!)

            true
        }

        b.postDialogLayout.setOnClickListener{
            dismiss()
        }
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layout = b.postDialogLayout
        if(ActivityCompat.checkSelfPermission(this.context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermission(view)
        } else hasWriteMemoryPermission = true
    }

    private fun setTheViewWithData(post: Post){
        b.authorTV.text = post.author
        b.descriptionTV.text = post.description
        b.titleTV.text = post.title
        b.likesTV.text = post.likes.toString()
        //b.postImg.setImageBitmap(post.content.toBitmap())
        b.postImg.load(GLOBALS.GET_IMG(GLOBALS.CONTENT_POST,id)){
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
        }
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

    private fun requestPermission(view: View){
        when {
            ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                layout.showSnackbar(view, getString(R.string.permission_granted), Snackbar.LENGTH_INDEFINITE, null) {}
            }

            ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                layout.showSnackbar(view, getString(R.string.permission_required), Snackbar.LENGTH_INDEFINITE, getString(R.string.ok)) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            hasWriteMemoryPermission = true
            saveImage(b.postImg.drawable.toBitmap())
        } else {
            hasWriteMemoryPermission = false
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }

    private fun View.showSnackbar(view: View, msg: String, length: Int, actionMessage: CharSequence?, action: (View) -> Unit) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this)
            }.show()
        } else snackbar.show()
    }

    private fun saveImage(bitmap: Bitmap) {
        //Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            context?.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(context!!,"Photo saved",Toast.LENGTH_LONG).show()
            // TODO: create the notification
        }
    }
}