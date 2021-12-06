package com.example.socialmedia.objects

import java.security.MessageDigest

object HashSHA256 {
    fun hash(string: String): String {
        val bytes = string.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}