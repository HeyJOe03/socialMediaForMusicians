package com.example.socialmedia.dialogs

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import coil.load
import coil.transform.CircleCropTransformation
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.databinding.DialogProfileEditBinding
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.lang.Exception

class ProfileEditDialog(
    private var onCloseEditDialog: ProfileEditDialog.SetOnEditDialogClose
) : DialogFragment() {
    private lateinit var b: DialogProfileEditBinding
    private var profilePic: Bitmap? = null
    private var locationByNetwork: Location? = null
    private var locationByGps: Location? = null


    private lateinit var locationManager: LocationManager
    //private lateinit var tvGpsLocation: TextView

    // binding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run{
            //initiate the binding here and pass the root to the dialog view
            b = DialogProfileEditBinding.inflate(layoutInflater).apply {
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
            val loc = getLocation()
            if(loc != null){
                (b.lonET as TextView).text = loc.longitude.toString()
                (b.latET as TextView).text = loc.latitude.toString()
            }
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

        b.btnClearCoord.setOnClickListener {
            (b.lonET as TextView).text = ""
            (b.latET as TextView).text = ""
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun getLocation() : Location?{
        var currentLocation: Location? = null
        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(!isLocationPermissionGranted()) return null

        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (hasGps) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, gpsLocationListener)
        }
        if (hasNetwork) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, networkLocationListener)
        }

        val lastKnownLocationByGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnownLocationByGps?.let {
            locationByGps = lastKnownLocationByGps
        }
        val lastKnownLocationByNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        lastKnownLocationByNetwork?.let {
            locationByNetwork = lastKnownLocationByNetwork
        }
        if (locationByGps != null && locationByNetwork != null) {
            if (locationByGps!!.accuracy > locationByNetwork!!.accuracy) {
                currentLocation = locationByGps
                //latitude = currentLocation?.latitude
                //longitude = currentLocation?.longitude
                // use latitude and longitude as per your need
            } else {
                currentLocation = locationByNetwork
                //latitude = currentLocation?.latitude
                //longitude = currentLocation?.longitude
                // use latitude and longitude as per your need
            }
        }

        return currentLocation

    }

    val gpsLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationByGps= location
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    val networkLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationByNetwork= location
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GLOBALS.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            b.profilePic.load(data?.data){
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
                transformations(CircleCropTransformation())
            }
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
        else if(requestCode == GLOBALS.LOCATION_PERMISSION_CODE && resultCode == Activity.RESULT_OK){
            getLocation()
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
        try{
            JSON.put("lat",b.latET.text.toString().toFloat())
            JSON.put("lon",b.lonET.text.toString().toFloat())
        }catch(e: Exception){
            JSON.put("lat",null)
            JSON.put("lon",null)
        }

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
            //JSONObject(),
            {
                //dismiss()
                if(profilePic != null)profilePicEditRequest()
                else dismiss()
            }
        ) { e -> e.printStackTrace()
            val dialog = ErrorDialog.newInstance(String(e.networkResponse.data))
            dialog.show(parentFragmentManager, "Error")}

        requestQueue.add(jsonObjectRequest)
    }

    private fun profilePicEditRequest(){
        val JSON = JSONObject()
        JSON.put("img",profilePic?.toBase64()) // if control made in the response of postEditProfileRequest

        val postUrl = GLOBALS.SERVER_PROFILE_EDIT_IMG
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            JSON,
            {
                dismiss()
            }
        ) { e -> e.printStackTrace()
            val dialog = ErrorDialog.newInstance(String(e.networkResponse.data))
            dialog.show(parentFragmentManager, "Error")}

        requestQueue.add(jsonObjectRequest)
    }

    private fun getMyProfileRequest(){

        val requestBody: JSONObject = JSONObject()

        val sharedPref : SharedPreferences? = activity?.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
        val userID = sharedPref!!.getLong(GLOBALS.SP_KEY_ID,-1)
        val hash_password = sharedPref.getString(GLOBALS.SP_KEY_PW,"")
        requestBody.put("id",userID)
        requestBody.put("hash_password",hash_password)

        val postUrl = GLOBALS.SERVER_PROFILE_GET_INFO
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            requestBody,
            { res ->
                //val string_profile_pic : String= res.getString("profile_pic")
                //profilePic = string_profile_pic.toBitmap()!!
                b.profilePic.load(GLOBALS.SERVER_PROFILE_PIC(userID)){
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder)
                    transformations(CircleCropTransformation())
                }

                (b.usernameET as TextView).text = res.getString("username")
                (b.emailET as TextView).text = res.getString("email")
                (b.nameET as TextView).text = res.getString("name")
                (b.latET as TextView).text = if (res.getString("lat") == "null")  "" else res.getString("lat")
                (b.lonET as TextView).text = if (res.getString("lon") == "null")  "" else res.getString("lon")
                (b.descriptionET as TextView).text = res.getString("description")
                (b.instrumentInterestedInET as TextView).text = res.getString("instrument_interested_in")

                val button_state : Boolean = res.getBoolean("is_looking_someone_to_play_with")

                //val bgState : Boolean = b.btnLookingForOtherPlayers.background.constantState == getDrawable(context!!, R.drawable.btn_looking_for_other_players)?.constantState

                if (!button_state) {
                    b.btnLookingForOtherPlayers.setBackgroundResource(R.drawable.btn_not_looking_for_other_players)
                    b.btnLookingForOtherPlayers.text = "I'm not looking for other people to play with"
                }
                else {
                    b.btnLookingForOtherPlayers.setBackgroundResource(R.drawable.btn_looking_for_other_players)
                    b.btnLookingForOtherPlayers.text = "I'm looking for other people to play with"
                }

            }
        ) { e -> e.printStackTrace()
            val dialog = ErrorDialog.newInstance(String(e.networkResponse.data))
            dialog.show(parentFragmentManager, "Error")
        }

        requestQueue.add(jsonObjectRequest)

    }

    private fun pickImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GLOBALS.IMAGE_REQUEST_CODE)
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), GLOBALS.LOCATION_PERMISSION_CODE)
            false
        } else true
    }

    private fun Bitmap.toBase64(): String? {
        val baos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    override fun onDestroy() {
        super.onDestroy()
        onCloseEditDialog.onEditDialogClose()
    }

    interface SetOnEditDialogClose{
        fun onEditDialogClose()
    }
}