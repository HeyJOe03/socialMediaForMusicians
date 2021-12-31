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
import coil.load
import com.example.socialmedia.GLOBALS
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
                onItemClickListener.onLongClickListener(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_card, viewGroup, false)
        return ViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.postImg.load(GLOBALS.POST_IMG(dataSet[position].id)){
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
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
        return Bitmap.createScaledBitmap(bitmap,width,height,false)
    }

    interface OnItemClickListener{
        fun onClickListener(position: Int)
        fun onLongClickListener(position: Int)
    }
}