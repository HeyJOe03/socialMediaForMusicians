package com.example.socialmedia.profileFragment.recycleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.dataClass.Post
import com.example.socialmedia.profileFragment.diffutils.PostsDiffutil

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

            itemView.setOnLongClickListener {
                onItemClickListener.onLongClickListener(layoutPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_post, viewGroup, false)
        return ViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.postImg.load(GLOBALS.POST_IMG(dataSet[position].id)){
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
            scale(Scale.FILL)

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

    interface OnItemClickListener{
        fun onClickListener(position: Int)
        fun onLongClickListener(position: Int)
    }
}