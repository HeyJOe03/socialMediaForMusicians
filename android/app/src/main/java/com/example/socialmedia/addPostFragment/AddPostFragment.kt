package com.example.socialmedia.addPostFragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.databinding.FragmentAddPostBinding
import com.example.socialmedia.databinding.PostPreview2Binding

class AddPostFragment : Fragment() {

    private var _binding: FragmentAddPostBinding? = null
    private val b get() = _binding!!
    private var hasCameraPermission: Boolean = false
    private var imagePreview: Bitmap? = null
    private var indexCurrentPostPreview: Int = 1
    private val nOfPreviews: Int = 2

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

        if(hasCameraPermission) openCamera()
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
        }
    }

    private fun runFragment(){
        val fragment : Fragment = when (indexCurrentPostPreview){
            1 -> PostPreview1Fragment(imagePreview)
            2 -> PostPreview2Fragment()
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
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GLOBALS.CAMERA_PHOTO_RESULT_CODE){
            val pic: Bitmap? = data?.getParcelableExtra("data")
            imagePreview = pic
            val fragment: PostPreview1Fragment = PostPreview1Fragment(imagePreview)
            val transaction = childFragmentManager.beginTransaction()
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
        var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(i,GLOBALS.CAMERA_PHOTO_RESULT_CODE)
    }
}