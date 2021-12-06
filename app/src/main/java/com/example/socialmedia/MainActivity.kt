package com.example.socialmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialmedia.databinding.ActivityMainBinding
import io.socket.client.Socket

const val EXTRA_MESSAGE = "com.example.socialmedia.MESSAGE"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mSocket: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        mSocket = SocketHandler.getSocket()

    }
}