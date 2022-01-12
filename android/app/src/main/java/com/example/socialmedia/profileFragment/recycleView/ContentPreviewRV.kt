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
import com.example.socialmedia.profileFragment.diffutils.RVMyDiffutil

class ContentPreviewRV(
    private var dataSet: List<Long>,
    val typeOfRV: String, // PUBLIC
    private val onRVRVItemClickListener: OnRVItemClickListener
) : RecyclerView.Adapter<ContentPreviewRV.ViewHolder>() {

    init {
        if(typeOfRV != "Instrument" && typeOfRV != "Post" && typeOfRV != "Sheet") throw Error("impossible find the class")
    }

    class ViewHolder(view: View, onRVItemClickListener: OnRVItemClickListener, typeOfRV: String) : RecyclerView.ViewHolder(view) {
        val postImg: ImageView
        //val postTitle: TextView

        init {

            postImg = view.findViewById(R.id.post_img)
            //postTitle = view.findViewById(R.id.post_title)
            itemView.setOnClickListener {
                onRVItemClickListener.onRVClickListener(layoutPosition, typeOfRV)
            }

            itemView.setOnLongClickListener {
                onRVItemClickListener.onRVLongClickListener(layoutPosition, typeOfRV)
                true
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_post_sheet_instrument_pic, viewGroup, false)
        return ViewHolder(view, onRVRVItemClickListener, typeOfRV)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val contentRQ = when{
            typeOfRV == "Sheet" -> "sheet"
            typeOfRV == "Instrument" -> "secondHandInstrument"
            typeOfRV == "Post" -> "post"
            else -> throw Error("request does not exist")
        }

        viewHolder.postImg.load(GLOBALS.SERVER + "/data/" + contentRQ + "/" + dataSet[position]){
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
            scale(Scale.FILL)
        }

    }

    fun setData(newData: List<Long>) {
        val diffCallback = RVMyDiffutil(dataSet, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        dataSet = newData
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    interface OnRVItemClickListener{
        fun onRVClickListener(position: Int,typeOfRV: String)
        fun onRVLongClickListener(position: Int,typeOfRV: String)
    }
}