package com.example.socialmedia.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import coil.load
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.databinding.ProfileEditDialogBinding
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class ProfileEditDialog : DialogFragment() {
    private lateinit var b: ProfileEditDialogBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var profilePic: Bitmap? = null
    //private var profileIsChanged = false

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

        getMyProfileRequest()

        // buttons

        b.layoutProfileEditDialog.setOnClickListener{
            dismiss()
        }

        b.btnGetCoord.setOnClickListener {

        }

        b.btnLookingForOtherPlayers.setOnClickListener {
            val bgState : Boolean = (b.btnLookingForOtherPlayers.background.constantState == resources.getDrawable(R.drawable.btn_looking_for_other_players)?.constantState)

            if (bgState) {
                b.btnLookingForOtherPlayers.setBackgroundResource(R.drawable.btn_not_looking_for_other_players)
                b.btnLookingForOtherPlayers.text = "I'm not looking for other people to play with"
            }
            else {
                b.btnLookingForOtherPlayers.setBackgroundResource(R.drawable.btn_looking_for_other_players)
                b.btnLookingForOtherPlayers.text = "I'm looking for other people to play with"
            }
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
            //profileIsChanged = true
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

        val is_looking_someone_to_play_with : Boolean = (b.btnLookingForOtherPlayers.background.constantState == resources.getDrawable(R.drawable.btn_looking_for_other_players)?.constantState)

        JSON.put("id",userID)
        JSON.put("username",b.usernameET.text.toString())
        JSON.put("hash_password",hash_password)
        JSON.put("email",b.emailET.text.toString())
        JSON.put("is_looking_someone_to_play_with",is_looking_someone_to_play_with)
        JSON.put("name",b.nameET.text.toString())
        JSON.put("lat",b.latET.text.toString().toFloat())
        JSON.put("lon",b.lonET.text.toString().toFloat())
        JSON.put("description",b.descriptionET.text.toString())
        JSON.put("instrument_interested_in",b.instrumentInterestedInET.text.toString())
        //if(profileIsChanged)JSON.put("profile_pic",profilePic?.toBase64())

        return JSON
    }

    private fun sendNewProfile(){
        val JSON = createJSONrequestEditProfile()
        postEditProfileRequest(JSON)
    }

    private fun postEditProfileRequest(requestBody: JSONObject){
        val postUrl = GLOBALS.SERVER + "/profile/edit"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            requestBody,
            {
                //dismiss()
                if(profilePic != null)profilePicEditRequest()
            }
        ) { e -> e.printStackTrace()
            val dialog = ErrorDialog.newInstance(e.message.toString())
            dialog.show(parentFragmentManager, "Error")}

        requestQueue.add(jsonObjectRequest)
    }

    private fun profilePicEditRequest(){
        val JSON = JSONObject()
        JSON.put("img",profilePic?.toBase64()) // if control made in the response of postEditProfileRequest

        val postUrl = GLOBALS.SERVER + "/profile/editpic"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            JSON,
            {
                dismiss()
            }
        ) { e -> e.printStackTrace()
            val dialog = ErrorDialog.newInstance(e.message.toString())
            dialog.show(parentFragmentManager, "Error")}

        requestQueue.add(jsonObjectRequest)
    }

    private fun getMyProfileRequest(){

        val requestBody: JSONObject = JSONObject()

        val sharedPref : SharedPreferences? = activity?.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
        val userID = sharedPref!!.getLong(GLOBALS.SP_KEY_ID,-1)
        val hash_password = sharedPref!!.getString(GLOBALS.SP_KEY_PW,"")
        requestBody.put("id",userID)
        requestBody.put("hash_password",hash_password)

        val postUrl = GLOBALS.SERVER + "/profile/myProfile"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            requestBody,
            { res ->
                //val string_profile_pic : String= res.getString("profile_pic")
                //profilePic = string_profile_pic.toBitmap()!!
                b.profilePic.load(GLOBALS.SERVER + "/profile/img/${userID}")

                (b.usernameET as TextView).text = res.getString("username")
                (b.emailET as TextView).text = res.getString("email")
                (b.nameET as TextView).text = res.getString("name")
                (b.latET as TextView).text = res.getString("lat")
                (b.lonET as TextView).text = res.getString("lon")
                (b.descriptionET as TextView).text = res.getString("description")
                (b.instrumentInterestedInET as TextView).text = res.getString("instrument_interested_in")

                val button_state : Boolean = res.getBoolean("is_looking_someone_to_play_with")

                //val bgState : Boolean = b.btnLookingForOtherPlayers.background.constantState == getDrawable(context!!, R.drawable.btn_looking_for_other_players)?.constantState

                if (button_state) {
                    b.btnLookingForOtherPlayers.setBackgroundResource(R.drawable.btn_not_looking_for_other_players)
                    b.btnLookingForOtherPlayers.text = "I'm not looking for other people to play with"
                }
                else {
                    b.btnLookingForOtherPlayers.setBackgroundResource(R.drawable.btn_looking_for_other_players)
                    b.btnLookingForOtherPlayers.text = "I'm looking for other people to play with"
                }

            }
        ) { e -> e.printStackTrace()
            val dialog = ErrorDialog.newInstance(e.message.toString())
            dialog.show(parentFragmentManager, "Error")
        }

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

    private fun String.toBitmap(): Bitmap? {
        Base64.decode(this, Base64.DEFAULT).apply {
            return BitmapFactory.decodeByteArray(this, 0, size)
        }
    }
}