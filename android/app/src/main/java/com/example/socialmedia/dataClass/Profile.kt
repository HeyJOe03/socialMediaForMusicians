package com.example.socialmedia.dataClass

data class Profile(
    val id:Long,
    var username: String,
    var email: String,
    var hash_password : String, // 30 char
    var is_blocked : Boolean?,
    var is_looking_someone_to_play_with : Boolean,
    var name: String,
    var lat: Float,
    var lon: Float,
    var description: String,
    var instrument_interested_in: String,
    var profile_pic: String,
    var created_at: Long?,
    var last_post: Long?,
    var last_instrument_offer: Long?,
    var last_music_sheet: Long?
)
