package com.example.socialmedia.searchFragment

import androidx.recyclerview.widget.DiffUtil
import com.example.socialmedia.dataClass.Post
import com.example.socialmedia.dataClass.UserSearch

class RVMySearchDiffutil(
    private val oldList: List<UserSearch>,
    private val newList: List<UserSearch>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            oldList[oldItemPosition].username != newList[newItemPosition].username -> false
            else -> true
        }
    }
}