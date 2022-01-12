package com.example.socialmedia.profileFragment.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.socialmedia.dataClass.Post

class RVMyDiffutil(
    private val oldList: List<Long>,
    private val newList: List<Long>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            else -> true
        }
    }
}