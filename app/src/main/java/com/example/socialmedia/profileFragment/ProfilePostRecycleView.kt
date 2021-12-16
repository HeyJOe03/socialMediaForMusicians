package com.example.socialmedia.profileFragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedia.R
import com.example.socialmedia.dataClass.Post
import java.lang.Exception

class ProfilePostRecycleView(
    private var dataSet: List<Post>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ProfilePostRecycleView.ViewHolder>() {

    class ViewHolder(view: View, onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val postImg: ImageView
        //val postTitle: TextView

        init {
            postImg = view.findViewById(R.id.post_img)
            //postTitle = view.findViewById(R.id.post_title)
            itemView.setOnClickListener {
                onItemClickListener.onClickListener(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_card, viewGroup, false)
        return ViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //viewHolder.postTitle.text = dataSet[position].description
        try {
            val img: Bitmap? = dataSet[position].content.toBitmap()
            if (img != null) // oldWidth : 400 = oldHeight : x
                viewHolder.postImg.setImageBitmap(resizeBitmap(img, 400, (img.height * 400) / img.width))

            viewHolder.postImg.contentDescription = dataSet[position].description
        } catch (e: Exception) {
            Log.println(Log.ERROR, "error", "null image conversion")
        }

    }

    private fun String.toBitmap(): Bitmap? {
        Base64.decode(this, Base64.DEFAULT).apply {
            return BitmapFactory.decodeByteArray(this, 0, size)
        }
    }

    fun setData(newData: List<Post>) {
        val diffCallback = PostsDiffutil(dataSet, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        dataSet = newData
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(
            bitmap,
            width,
            height,
            false
        )
    }

    interface OnItemClickListener{
        fun onClickListener(position: Int)
    }
}


/*
      *** reference source developer.android.com ***
      Bitmap createScaledBitmap (Bitmap src, int dstWidth, int dstHeight, boolean filter)
          Creates a new bitmap, scaled from an existing bitmap, when possible. If the specified
          width and height are the same as the current width and height of the source bitmap,
          the source bitmap is returned and no new bitmap is created.

      Parameters
          src Bitmap : The source bitmap.
              This value must never be null.

      dstWidth int : The new bitmap's desired width.
      dstHeight int : The new bitmap's desired height.
      filter boolean : true if the source should be filtered.

      Returns
          Bitmap : The new scaled bitmap or the source bitmap if no scaling is required.

      Throws
          IllegalArgumentException : if width is <= 0, or height is <= 0
  */