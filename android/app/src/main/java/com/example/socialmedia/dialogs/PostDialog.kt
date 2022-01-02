package com.example.socialmedia.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import coil.load
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.dataClass.Post
import com.example.socialmedia.databinding.PostDialogBinding

class PostDialog(
    val post: Post
): DialogFragment() {

    private var mView: View? = null

    private lateinit var b: PostDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            //initiate the binding here and pass the root to the dialog view
            b = PostDialogBinding.inflate(layoutInflater).apply {
                //reference layout elements by name freely here
            }
            AlertDialog.Builder(this).apply {
                setView(b.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        b.authorTV.text = post.author
        b.descriptionTV.text = post.description
        b.titleTV.text = post.title
        //b.postImg.setImageBitmap(post.content.toBitmap())
        b.postImg.load(GLOBALS.POST_IMG(post.id)){
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
        }

        b.postDialogLayout.setOnClickListener{
            dismiss()
        }

        return mView
    }

}