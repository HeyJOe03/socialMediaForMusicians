package com.example.socialmedia.profileFragment.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.socialmedia.dataClass.Instrument
import com.example.socialmedia.dataClass.Sheet

class InstrumentDiffutil(
    private val oldList: List<Instrument>,
    private val newList: List<Instrument>
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
            oldList[oldItemPosition].instrument_description != newList[newItemPosition].instrument_description -> false
            oldList[oldItemPosition].price != newList[newItemPosition].price -> false
            else -> true
        }
    }
}