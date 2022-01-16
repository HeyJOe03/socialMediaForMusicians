package com.example.socialmedia.searchFragment

import android.graphics.drawable.GradientDrawable
import android.icu.lang.UCharacter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedia.R
import com.example.socialmedia.dataClass.UserSearch
import com.example.socialmedia.databinding.FragmentSearchBinding
import com.example.socialmedia.profileFragment.ContentPreviewRV

class SearchFragment : Fragment(), SearchRV.OnRVSearchUserClickListener {

    private lateinit var adapterUser: SearchRV
    private var _binding: FragmentSearchBinding? = null
    private val b get() = _binding!!
    private val testList = listOf<UserSearch>(UserSearch(1,"uno"), UserSearch(2,"due"),UserSearch(3,"tre"))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        val linearLayoutMenager = LinearLayoutManager(context!!,LinearLayoutManager.VERTICAL,false)
        adapterUser = SearchRV(testList,this)

        b.searchResultsRV.apply{
            layoutManager = linearLayoutMenager
            adapter = adapterUser
        }


    }

    override fun onRVClickListener(position: Int) {
        TODO("Not yet implemented")
    }

}