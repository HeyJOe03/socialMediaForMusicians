package com.example.socialmedia.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.socialmedia.dataClass.Post
import com.example.socialmedia.databinding.PostEditDialogBinding

class PostEditDialog(
    private val post: Post
): DialogFragment() {
    private var mView: View? = null
    private lateinit var b: PostEditDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run{
            //initiate the binding here and pass the root to the dialog view
            b = PostEditDialogBinding.inflate(layoutInflater).apply {
                //reference layout elements by name freely here
            }
            AlertDialog.Builder(this).apply {
                setView(b.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.println(Log.ERROR,"post in long click",post.toString())

        (b.authorET as TextView).text = post.author
        (b.titleET as TextView).text = post.title
        (b.descriptionET as TextView).text = post.description

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b.btnEdit.setOnClickListener {
            
        }

        b.btnDelete.setOnClickListener{

        }

    }
}