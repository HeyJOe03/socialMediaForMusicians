package com.example.socialmedia.dataClass

import java.text.DateFormat
import java.util.*

data class Post(
    val id: Long,
    val description: String,
    //val content: String,
    val posted_by: Long?,
    val created_at: String?,
    val last_update_at: String?,
    val likes :Long,
    val author: String,
    val title: String
)
