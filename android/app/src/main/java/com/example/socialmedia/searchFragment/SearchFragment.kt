package com.example.socialmedia.searchFragment

import android.graphics.drawable.GradientDrawable
import android.icu.lang.UCharacter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.UiThread
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedia.R
import com.example.socialmedia.dataClass.UserSearch
import com.example.socialmedia.databinding.FragmentSearchBinding
import com.example.socialmedia.objects.SocketHandler.mSocket
import com.example.socialmedia.profileFragment.ContentPreviewRV
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken




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
        adapterUser = SearchRV(emptyList(),this)

        b.searchResultsRV.layoutManager = linearLayoutMenager
        b.searchResultsRV.adapter = adapterUser


        b.searchET.addTextChangedListener { it ->
            mSocket.emit("search-user",it.toString())
        }

        mSocket.on("search-user"){ args ->
            if(args[0] != null){
                val gson: Gson = Gson()

                val sType = object : TypeToken<List<UserSearch>>() { }.type
                val list: List<UserSearch>  = gson.fromJson(args[0].toString(), sType)

                activity!!.runOnUiThread {
                    adapterUser.setData(list)
                }
            }
        }

    }

    override fun onRVClickListener(position: Int) {
        TODO("Not yet implemented")
    }


}