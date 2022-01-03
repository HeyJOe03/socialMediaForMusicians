package com.example.socialmedia.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.socialmedia.databinding.ProfileEditDialogBinding

class ProfileEditDialog : DialogFragment() {
    private lateinit var b: ProfileEditDialogBinding

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // buttons

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}