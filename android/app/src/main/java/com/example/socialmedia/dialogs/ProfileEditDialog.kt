package com.example.socialmedia.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.databinding.ProfileEditDialogBinding
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class ProfileEditDialog : DialogFragment() {
    private lateinit var b: ProfileEditDialogBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var profilePic: Bitmap

    // binding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run{
            //initiate the binding here and pass the root to the dialog view
            b = ProfileEditDialogBinding.inflate(layoutInflater).apply {
                //reference layout elements by name freely here
            }
            AlertDialog.Builder(this).apply {
                setView(b.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // request for the profile infos

        // put the information in the right place


        // buttons
        b.btnGetCoord.setOnClickListener {

        }

        b.btnLookingForOtherPlayers.setOnClickListener {

        }

        b.btnEditSave.setOnClickListener {
            sendNewProfile()
        }

        b.profilePic.setOnClickListener {
            pickImage()
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GLOBALS.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            b.profilePic.setImageURI(data?.data)
            profilePic = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        requireContext().contentResolver,
                        data?.data!!
                    )
                )
            } else {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data?.data!!)
            }
        }
    }

    private fun createJSONrequestEditProfile(): JSONObject{
        val JSON: JSONObject = JSONObject()

        val sharedPreferences : SharedPreferences = activity!!.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
        val userID = sharedPreferences.getLong(GLOBALS.SP_KEY_ID,-1)
        val hash_password = sharedPreferences.getString(GLOBALS.SP_KEY_PW,"")
        JSON.put("id",userID)
        JSON.put("hash_password",hash_password)
        JSON.put("email",b.emailET.text.toString())
        JSON.put("is_looking_someone_to_play_with",true)
        JSON.put("name",b.nameET.text.toString())
        JSON.put("lat",b.latET.text.toString().toFloat())
        JSON.put("lon",b.lonET.text.toString().toFloat())
        JSON.put("description",b.descriptionET.text.toString())
        JSON.put("instrument_interested_in",b.instrumentInterestedInET.text.toString())
        JSON.put("profile_pic",profilePic.toBase64())

        return JSON
    }

    private fun sendNewProfile(){
        val JSON = createJSONrequestEditProfile()
        postEditProfileRequest(JSON)
    }

    private fun postEditProfileRequest(requestBody: JSONObject){
        val postUrl = GLOBALS.SERVER + "/profile/update"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            requestBody,
            {
                dismiss()
            }
        ) { e -> e.printStackTrace()
            val dialog = ErrorDialog.newInstance(e.message.toString())
            dialog.show(parentFragmentManager, "Error")}

        requestQueue.add(jsonObjectRequest)
    }

    private fun pickImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GLOBALS.IMAGE_REQUEST_CODE)
    }

    private fun Bitmap.toBase64(): String? {
        val baos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}