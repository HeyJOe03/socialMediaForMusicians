package com.example.socialmedia.addPostFragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.socialmedia.R
import com.google.android.material.imageview.ShapeableImageView

class PostPreview1Fragment(
    private val img: Bitmap?
) : Fragment() {
    private lateinit var imageView: ShapeableImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub_fragment_post_preview_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.post_preview_image)
        imageView.setImageBitmap(img)
    }

}