package com.example.socialmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.socket.client.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var mSocket: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        mSocket = SocketHandler.getSocket()

    }
}