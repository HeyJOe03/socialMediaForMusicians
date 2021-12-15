package com.example.socialmedia.ProfileActivities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedia.R

class ProfilePostRecycleView(
    private val dataSet: List<String> //TODO: change that
) : RecyclerView.Adapter<ProfilePostRecycleView.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val postImg: ImageView
        val postTitle: TextView
        init{
            postImg = view.findViewById(R.id.post_img)
            postTitle = view.findViewById(R.id.post_title)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.postTitle.text = dataSet[position]
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}