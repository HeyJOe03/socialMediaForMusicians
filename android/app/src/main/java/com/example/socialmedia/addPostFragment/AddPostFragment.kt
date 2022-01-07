package com.example.socialmedia.addPostFragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.dataClass.Post
import com.example.socialmedia.databinding.FragmentAddPostBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*

class AddPostFragment(
    private val setOnClose: AddPostFragment.SetOnClose
) : Fragment() {

    private var _binding: FragmentAddPostBinding? = null
    private val b get() = _binding!!
    private var hasCameraPermission: Boolean = false
    private var indexCurrentPostPreview: Int = 1
    private val nOfPreviews: Int = 2
    private var message = ""
    private var post: Post? = null

    private lateinit var layout: View

    private var imagePreview: Bitmap? = null

    private val postPreview2Fragment: PostPreview2Fragment = PostPreview2Fragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddPostBinding.bind(view)
        layout = b.layoutMainAddPost

        post = null

        if(ActivityCompat.checkSelfPermission(this.context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermission(view)
        } else hasCameraPermission = true

        b.btnNext.setOnClickListener{
            indexCurrentPostPreview++
            setButtonsNextBack()
            runFragment()
        }

        b.btnBack.setOnClickListener {
            indexCurrentPostPreview--
            setButtonsNextBack()
            runFragment()
        }

        b.btnLoadFromCamera.setOnClickListener {
            if(hasCameraPermission) openCamera()
            else requestPermission(view)
        }

        b.btnLoadFromGallery.setOnClickListener {
            pickImage()
        }

        b.btnPost.setOnClickListener {
            val data = postPreview2Fragment.getData()
            data.put("content",imagePreview?.toBase64())
            val sharedPreferences : SharedPreferences = activity!!.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
            val userID = sharedPreferences.getLong(GLOBALS.SP_KEY_ID,-1)
            val hash_password = sharedPreferences.getString(GLOBALS.SP_KEY_PW,"")
            data.put("posted_by",userID)
            data.put("hash_password",hash_password)

            loadPostRequest(data)
        }
    }

    private fun pickImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GLOBALS.IMAGE_REQUEST_CODE)
    }

    private fun loadPostRequest(postData: JSONObject){

        val postUrl = GLOBALS.SERVER + "/post/load"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)


        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            {it ->
                post = Post(it.getLong("id"),postData.getString("description"),null, null,null,postData.getString("author"),postData.getString("title"))
                message = "good"
                //parentFragmentManager.beginTransaction().remove(this).commit()
            }
        ) { error ->

            post = Post(-1,postData.getString("description"),null, null,null,postData.getString("author"),postData.getString("title"))
            message = String(error.networkResponse.data)
            error.printStackTrace()
            //parentFragmentManager.beginTransaction().remove(this).commit()
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun setButtonsNextBack(){
        b.btnBack.isClickable = true
        b.btnBack.visibility = View.VISIBLE
        b.btnNext.isClickable = true
        b.btnNext.visibility = View.VISIBLE
        if(indexCurrentPostPreview == 1){
            b.btnBack.isClickable = false
            b.btnBack.visibility = View.INVISIBLE
        } else if(indexCurrentPostPreview == nOfPreviews){
            b.btnNext.isClickable = false
            b.btnNext.visibility = View.INVISIBLE
            b.btnPost.visibility = View.VISIBLE
        }

        if(indexCurrentPostPreview != nOfPreviews){
            b.btnPost.visibility = View.INVISIBLE
        }
    }

    private fun runFragment(){
        val fragment: Fragment = when (indexCurrentPostPreview){
            1 -> PostPreview1Fragment(imagePreview)
            2 -> postPreview2Fragment
            else -> throw Exception("indexCurrentPostPreview out of range")
        }

        val transaction = childFragmentManager.beginTransaction()
        transaction.apply {
            replace(R.id.fl_addPost_wrapper, fragment)
            commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GLOBALS.CAMERA_PHOTO_RESULT_CODE){
            val pic: Bitmap? = data?.getParcelableExtra("data")
            imagePreview = pic
            val fragment = PostPreview1Fragment(imagePreview)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.apply {
                replace(R.id.fl_addPost_wrapper, fragment)
                commit()
            }

            b.btnLoadFromGallery.visibility = View.GONE
            b.btnLoadFromCamera.visibility = View.GONE
            b.btnNext.visibility = View.VISIBLE
        }
        else if(requestCode == GLOBALS.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            //image.setImageURI(data?.data)
            val imagePreview = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, data?.data!!))
            } else {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data?.data!!)
            }

            val fragment = PostPreview1Fragment(imagePreview)
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.apply {
                replace(R.id.fl_addPost_wrapper, fragment)
                commit()
            }

            b.btnLoadFromGallery.visibility = View.GONE
            b.btnLoadFromCamera.visibility = View.GONE
            b.btnNext.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        setOnClose.onClose(message,post)
        indexCurrentPostPreview = 1
    }

    private fun openCamera(){
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(i,GLOBALS.CAMERA_PHOTO_RESULT_CODE)
    }

    private fun Bitmap.toBase64(): String? {
        val baos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun requestPermission(view: View){
        when {
            ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_granted),
                    Snackbar.LENGTH_INDEFINITE,
                    null
                ) {}
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                Manifest.permission.CAMERA
            ) -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok)
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }

    private fun View.showSnackbar(view: View, msg: String, length: Int, actionMessage: CharSequence?, action: (View) -> Unit) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this)
            }.show()
        } else {
            snackbar.show()
        }
    }

    interface SetOnClose{
        fun onClose(message: String, post: Post?)
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                hasCameraPermission = true
                openCamera()
            } else {
                hasCameraPermission = false
                parentFragmentManager.beginTransaction().remove(this).commit()
            }
        }
}