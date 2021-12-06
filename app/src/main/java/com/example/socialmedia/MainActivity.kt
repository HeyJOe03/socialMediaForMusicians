package com.example.socialmedia

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.socialmedia.databinding.ActivityMainBinding
import com.example.socialmedia.objects.SocketHandler
import io.socket.client.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences
    private var ID: Long = -1

    private lateinit var mSocket: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = this.getSharedPreferences(GLOBALS.SHARED_PREF_ID_USER,Context.MODE_PRIVATE)
        ID = sharedPref.getLong(GLOBALS.SP_KEY_ID,-1)

        if(ID == (-1).toLong()) { // if the user has not been saved locally
            startActivity(Intent(this, LogInActivity::class.java)) // starts the login page
            this.finish() //close the activity
            return //end the function
        }

        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        mSocket = SocketHandler.getSocket()

    }
}