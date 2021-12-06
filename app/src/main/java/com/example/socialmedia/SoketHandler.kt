package com.example.socialmedia

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SoketHandler {
        lateinit var mSocket: Socket

        @Synchronized
        fun setSocket() {
            try {
                mSocket = IO.socket(GLOBALS.SERVER)
            } catch (e: URISyntaxException) {
            }
        }

        @Synchronized
        fun getSocket(): Socket {
            return mSocket
        }

        @Synchronized
        fun establishConnection() {
            mSocket.connect()
        }

        @Synchronized
        fun closeConnection() {
            mSocket.disconnect()
        }
}