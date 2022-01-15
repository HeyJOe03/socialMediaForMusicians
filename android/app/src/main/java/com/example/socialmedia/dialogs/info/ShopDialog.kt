package com.example.socialmedia.dialogs.info

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
import com.example.socialmedia.dataClass.Shop
import com.example.socialmedia.databinding.DialogShopBinding
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class ShopDialog(
    val id: Long
): DialogFragment() {

    private val gson: Gson = Gson()

    private var mView: View? = null

    private lateinit var b: DialogShopBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            //initiate the binding here and pass the root to the dialog view
            b = DialogShopBinding.inflate(layoutInflater).apply {
                //reference layout elements by name freely here
            }
            AlertDialog.Builder(this).apply {
                setView(b.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setTheViewWithData(Shop(-1,0,0.0f, null))

        shopRequest()

        b.shopDialogLayout.setOnClickListener{
            dismiss()
        }
        return mView
    }

    private fun setTheViewWithData(shop: Shop){
        b.instrumentDescriptionTV.text = shop.instrument_description
        b.priceTV.text = shop.price.toString()
        b.likesTV.text = shop.likes.toString()
        b.shopImg.load(GLOBALS.GET_IMG(GLOBALS.CONTENT_SHOP,id)){
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
        }
    }

    private fun shopRequest() {

        val shopUrl = GLOBALS.INFO_DATA(GLOBALS.CONTENT_SHOP)
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val shopData = JSONObject()
        try {
            shopData.put("id", id)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            shopUrl,
            shopData,
            { response ->
                val s = (response as JSONObject).toString()
                val shop = gson.fromJson(s,Shop::class.java)
                setTheViewWithData(shop)
            }
        ) { error ->
            error.printStackTrace()
            Log.println(Log.ERROR,"error","error")
        }

        requestQueue.add(jsonObjectRequest)
    }

}