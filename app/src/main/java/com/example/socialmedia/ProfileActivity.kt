package com.example.socialmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialmedia.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var b: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(b.root)
    }
}