package com.example.socialmedia.ProfileActivities

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProfilePostRecycleView(
    private val dataSet: Array<String> //TODO: change that
) : RecyclerView.Adapter<ProfilePostRecycleView.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}