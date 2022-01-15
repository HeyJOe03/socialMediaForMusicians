package com.example.socialmedia.dialogs

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
import com.example.socialmedia.dataClass.Shop
import com.example.socialmedia.databinding.DialogShopEditBinding
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class ShopEditDialog(
    private val id: Long,
    private val setOnDismiss: SetOnDismiss
): DialogFragment() {
    private var mView: View? = null
    private lateinit var b: DialogShopEditBinding
    private val gson: Gson = Gson()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run{
            //initiate the binding here and pass the root to the dialog view
            b = DialogShopEditBinding.inflate(layoutInflater).apply {
                //reference layout elements by name freely here
            }
            AlertDialog.Builder(this).apply {
                setView(b.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setTheViewWithData(Shop(-1,0,0.0f,null))
        shopRequest()

        b.btnEdit.setOnClickListener {
            updateShopRequest()
        }

        b.btnDelete.setOnClickListener{
            deleteShopRequest()
        }

        b.shopEditDialogLayout.setOnClickListener {
            dismiss()
        }

        return mView
    }

    private fun deleteShopRequest() {

        val shopUrl = GLOBALS.DELETE_DATA(GLOBALS.CONTENT_SHOP)
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val shopData = JSONObject()
        shopData.put("id",id)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            shopUrl,
            shopData,
            { dismiss() }
        ) { error ->
            error.printStackTrace()
            dismiss()
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun updateShopRequest() {

        val shopUrl = GLOBALS.UPDATE_DATA(GLOBALS.CONTENT_SHOP)
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val shopData = JSONObject()
        shopData.put("id", id)
        shopData.put("price",b.priceET.text.toString())
        shopData.put("instrument_description",b.instrumentDescriptionET.text.toString())

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            shopUrl,
            shopData,
            { dismiss() }
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

    private fun setTheViewWithData(shop: Shop){
        (b.priceET as TextView).text = shop.price.toString()
        (b.instrumentDescriptionET as TextView).text = shop.instrument_description
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

    interface SetOnDismiss{
        fun onDismiss()
    }
}