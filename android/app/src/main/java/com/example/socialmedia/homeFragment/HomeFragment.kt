package com.example.socialmedia.homeFragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedia.ContentRecycleView
import com.example.socialmedia.GLOBALS
import com.example.socialmedia.R
import com.example.socialmedia.databinding.FragmentHomeBinding
import com.example.socialmedia.databinding.FragmentProfileBinding

class HomeFragment : Fragment(), ContentRecycleView.OnRVItemClickListener {

    private lateinit var linearLayoutMenager : LinearLayoutManager
    private lateinit var adapterPost: ContentRecycleView
    private lateinit var adapterShop: ContentRecycleView
    private lateinit var adapterSheet: ContentRecycleView

    private var _binding: FragmentHomeBinding? = null
    private val b get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        linearLayoutMenager = LinearLayoutManager(context!!,LinearLayoutManager.VERTICAL,false)
        adapterShop = ContentRecycleView(emptyList(),GLOBALS.CONTENT_SHOP,this)
        adapterPost = ContentRecycleView(listOf(1,35,36),GLOBALS.CONTENT_POST,this)
        adapterSheet = ContentRecycleView(emptyList(),GLOBALS.CONTENT_SHEET,this)

        b.refreshLayout.setOnRefreshListener {
            Handler(Looper.myLooper()!!).postDelayed(
                { // Runnable
                    refresh()
                    b.refreshLayout.isRefreshing = false
                },
                1000,
            ) // This is how you can choose when it will end
        }

        b.homeRV.apply {
            layoutManager = linearLayoutMenager
            adapter = adapterPost
        }

    }

    private fun refresh(){

    }

    override fun onRVClickListener(position: Int, typeOfRV: String) {
        return
    }

    override fun onRVLongClickListener(position: Int, typeOfRV: String) {
        return
    }
}