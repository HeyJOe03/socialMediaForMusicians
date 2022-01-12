package com.example.socialmedia.profileFragment.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.socialmedia.dataClass.Post
import com.example.socialmedia.dataClass.Sheet

class SheetDiffutil(
    private val oldList: List<Sheet>,
    private val newList: List<Sheet>
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
            oldList[oldItemPosition].author != newList[newItemPosition].author -> false
            oldList[oldItemPosition].title != newList[newItemPosition].title -> false
            else -> true
        }
    }
}