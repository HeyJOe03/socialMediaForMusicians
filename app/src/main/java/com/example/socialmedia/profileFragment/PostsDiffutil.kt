package com.example.socialmedia.profileFragment

import androidx.recyclerview.widget.DiffUtil
import com.example.socialmedia.dataClass.Post

class PostsDiffutil(
    private val oldList: List<Post>,
    private val newList: List<Post>
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
            oldList[oldItemPosition].description != newList[newItemPosition].description -> false
            oldList[oldItemPosition].content != newList[newItemPosition].content -> false
            oldList[oldItemPosition].posted_by != newList[newItemPosition].posted_by -> false
            oldList[oldItemPosition].created_at != newList[newItemPosition].created_at -> false
            oldList[oldItemPosition].last_update_at != newList[newItemPosition].last_update_at -> false
            oldList[oldItemPosition].author != newList[newItemPosition].author -> false
            oldList[oldItemPosition].title != newList[newItemPosition].title -> false
            else -> true
        }
    }
}