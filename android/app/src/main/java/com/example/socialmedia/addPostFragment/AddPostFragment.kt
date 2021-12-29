package com.example.socialmedia.addPostFragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import coil.load
import coil.transform.CircleCropTransformation
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.databinding.FragmentAddPostBinding
import kotlinx.coroutines.internal.artificialFrame
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*

class AddPostFragment : Fragment() {

    private var _binding: FragmentAddPostBinding? = null
    private val b get() = _binding!!
    private var hasCameraPermission: Boolean = false
    private var indexCurrentPostPreview: Int = 1
    private val nOfPreviews: Int = 2

    private var imagePreview: Bitmap? = null

    private val postPreview2Fragment: PostPreview2Fragment = PostPreview2Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(ActivityCompat.checkSelfPermission(this.context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA),GLOBALS.CAMERA_PERMISSION_REQUEST_CODE)
        } else hasCameraPermission = true

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddPostBinding.bind(view)

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

        b.btnPost.setOnClickListener {
            val data = postPreview2Fragment.getData()
            data.put("content",imagePreview?.toBase64())
            val sharedPreferences : SharedPreferences = activity!!.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
            val userID = sharedPreferences.getLong(GLOBALS.SP_KEY_ID,-1)
            data.put("posted_by",userID)

            profileRequest(data)
            activity?.onBackPressed()
            // TODO: fixme sei una testa di cazzo devi chiudere sta cazzo di attività ciaone
        }

        if(hasCameraPermission) openCamera()
    }

    private fun profileRequest(postData: JSONObject){
        val postUrl = GLOBALS.SERVER + "/post/load"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            {}
        ) { error ->
            error.printStackTrace()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == GLOBALS.CAMERA_PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            hasCameraPermission = true
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openCamera(){
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(i,GLOBALS.CAMERA_PHOTO_RESULT_CODE)
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun Bitmap.toBase64(): String? {
        val baos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}