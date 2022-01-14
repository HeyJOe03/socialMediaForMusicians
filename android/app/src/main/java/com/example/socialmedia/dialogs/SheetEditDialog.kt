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
import com.example.socialmedia.dataClass.Sheet
import com.example.socialmedia.databinding.DialogSheetEditBinding
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SheetEditDialog(
    private val id: Long,
    private val setOnDismiss: SetOnDismiss
): DialogFragment() {
    private var mView: View? = null
    private lateinit var b: DialogSheetEditBinding
    private val gson: Gson = Gson()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run{
            //initiate the binding here and pass the root to the dialog view
            b = DialogSheetEditBinding.inflate(layoutInflater).apply {
                //reference layout elements by name freely here
            }
            AlertDialog.Builder(this).apply {
                setView(b.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setTheViewWithData(Sheet(-1,"",null,0,null))
        sheetRequest()

        b.btnEdit.setOnClickListener {
            updateSheetRequest()
        }

        b.btnDelete.setOnClickListener{
            deleteSheetRequest()
        }

        b.sheetEditDialogLayout.setOnClickListener {
            dismiss()
        }

        return mView
    }

    private fun deleteSheetRequest() {

        val sheetUrl = GLOBALS.DELETE_DATA(GLOBALS.CONTENT_SHEET)
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val sheetData = JSONObject()
        sheetData.put("id",id)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            sheetUrl,
            sheetData,
            { dismiss() }
        ) { error ->
            error.printStackTrace()
            dismiss()
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun updateSheetRequest() {

        val sheetUrl = GLOBALS.UPDATE_DATA(GLOBALS.CONTENT_SHEET)
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val sheetData = JSONObject()
        sheetData.put("id", id)
        sheetData.put("author",b.authorET.text.toString())
        sheetData.put("title",b.titleET.text.toString())
        sheetData.put("description",b.descriptionET.text.toString())

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            sheetUrl,
            sheetData,
            {response -> dismiss() }
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

    private fun setTheViewWithData(sheet: Sheet){
        (b.authorET as TextView).text = sheet.author
        (b.titleET as TextView).text = sheet.title
        (b.descriptionET as TextView).text = sheet.description
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

    interface SetOnDismiss{
        fun onDismiss()
    }
}