package com.example.socialmedia

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.SearchEvent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.socialmedia.addPostFragment.AddPostFragment
import com.example.socialmedia.databinding.ActivityMainBinding
import com.example.socialmedia.profileFragment.ProfileFragment
import com.example.socialmedia.loginsigninActivities.LogInActivity
import com.example.socialmedia.homeFragment.HomeFragment
import com.example.socialmedia.objects.SocketHandler
import com.example.socialmedia.searchFragment.SearchFragment
import com.example.socialmedia.shopFragment.ShopFragment
import io.socket.client.Socket

class MainActivity : AppCompatActivity(), AddPostFragment.SetOnClose {

    private lateinit var b: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences
    private var userID: Long = -1

    private lateinit var mSocket: Socket

    override fun onClose(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        b.navMenu.selectedItemId = R.id.ic_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        sharedPref = this.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER, Context.MODE_PRIVATE)
        userID = sharedPref.getLong(GLOBALS.SP_KEY_ID, -1)

        if (userID == (-1).toLong()) { // if the user has not been saved locally
            startActivity(Intent(this, LogInActivity::class.java)) // starts the login page
            this.finish() //close the activity
            return //end the function
        }

        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        mSocket = SocketHandler.getSocket()
        mSocket.emit(GLOBALS.S_MY_ID,userID)

        val profileFragment = ProfileFragment(userID)
        val homeFragment = HomeFragment()
        val addPostFragment = AddPostFragment(this)
        val shopFragment = ShopFragment()
        val searchFragment = SearchFragment()

        makeCurrentFragment(homeFragment)
        b.navMenu.selectedItemId = R.id.ic_home

        b.navMenu.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.ic_profile -> makeCurrentFragment(profileFragment)
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_add -> makeCurrentFragment(addPostFragment)
                R.id.ic_shop -> makeCurrentFragment(shopFragment)
                R.id.ic_search -> makeCurrentFragment(searchFragment)
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logOutMenu -> {
                logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }

    private fun logOut(){
        sharedPref.edit().remove(GLOBALS.SP_KEY_ID).apply()
        startActivity(Intent(this, LogInActivity::class.java))
        this.finish()
    }
}