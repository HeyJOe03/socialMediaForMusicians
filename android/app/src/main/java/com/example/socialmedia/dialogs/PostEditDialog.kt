package com.example.socialmedia.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.socialmedia.dataClass.Post
import com.example.socialmedia.databinding.PostEditDialogBinding

class PostEditDialog(
    private val post: Post
): DialogFragment() {
    private var mView: View? = null
    private lateinit var b: PostEditDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mView
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}