package com.example.socialmedia.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.socialmedia.R

class ErrorDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.registration_error_dialog, container, false)
    }

    private lateinit var errorTV : TextView
    private lateinit var errorText : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorTV = view.findViewById(R.id.errorTV)
        errorTV.text = errorText
        val box = view.findViewById<View>(R.id.registration_error_dialog_layout)
        box.setOnClickListener {
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        errorText = requireArguments().getString(ARGUMENT_ACTION).toString()
    }

    companion object {
        private const val ARGUMENT_ACTION = "ARGUMENT_ACTION"

        fun newInstance(action: String) : ErrorDialog{
            return ErrorDialog().apply {
                arguments = bundleOf(ARGUMENT_ACTION to action)
            }
        }
    }

}