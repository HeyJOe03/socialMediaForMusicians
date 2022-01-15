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
import com.example.socialmedia.dataClass.Sheet
import com.example.socialmedia.databinding.DialogSheetBinding
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class SheetDialog(
    val id: Long
): DialogFragment() {

    private val gson: Gson = Gson()

    private var mView: View? = null

    private lateinit var b: DialogSheetBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            //initiate the binding here and pass the root to the dialog view
            b = DialogSheetBinding.inflate(layoutInflater).apply {
                //reference layout elements by name freely here
            }
            AlertDialog.Builder(this).apply {
                setView(b.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setTheViewWithData(Sheet(-1,"",null,0, null))

        sheetRequest()

        b.sheetDialogLayout.setOnClickListener{
            dismiss()
        }
        return mView
    }

    private fun setTheViewWithData(sheet: Sheet){
        b.authorTV.text = sheet.author
        b.descriptionTV.text = sheet.description
        b.titleTV.text = sheet.title
        b.likesTV.text = sheet.likes.toString()
        //b.sheetImg.setImageBitmap(sheet.content.toBitmap())
        b.sheetImg.load(GLOBALS.GET_IMG(GLOBALS.CONTENT_SHEET,id)){
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
        }
    }

    private fun sheetRequest() {

        val sheetUrl = GLOBALS.INFO_DATA(GLOBALS.CONTENT_SHEET)
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val sheetData = JSONObject()
        try {
            sheetData.put("id", id)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            sheetUrl,
            sheetData,
            { response ->
                val s = (response as JSONObject).toString()
                val sheet = gson.fromJson(s,Sheet::class.java)
                setTheViewWithData(sheet)
            }
        ) { error ->
            error.printStackTrace()
            Log.println(Log.ERROR,"error","error")
        }

        requestQueue.add(jsonObjectRequest)
    }

}