package com.example.socialmedia.searchFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.dataClass.UserSearch
import com.google.android.material.imageview.ShapeableImageView

class SearchRV(
    var dataSet: List<UserSearch>,
    private val onRVSearchUserClickListener: OnRVSearchUserClickListener
) : RecyclerView.Adapter<SearchRV.ViewHolder>() {

    class ViewHolder(view: View, onRVSearchUserClickListener: OnRVSearchUserClickListener) : RecyclerView.ViewHolder(view) {
        val profilePic: ShapeableImageView
        val usernameTV: TextView
        val clickableLayout: ConstraintLayout

        init {
            profilePic = view.findViewById(R.id.profile_picture)
            usernameTV = view.findViewById(R.id.username_TV)
            clickableLayout = view.findViewById(R.id.clickable_layout_search_user)

            clickableLayout.setOnClickListener{
                // TODO: add listener
                onRVSearchUserClickListener.onRVClickListener(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_search_user, viewGroup, false)
        return ViewHolder(view, onRVSearchUserClickListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.profilePic.load(GLOBALS.SERVER_PROFILE_PIC(dataSet[position].id)){
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
            scale(Scale.FIT)
        }

        viewHolder.usernameTV.text = dataSet[position].username

    }

    fun setData(newData: List<UserSearch>) {
        val diffCallback = RVMySearchDiffutil(dataSet, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        dataSet = newData
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    interface OnRVSearchUserClickListener{
        fun onRVClickListener(position: Int)
    }
}