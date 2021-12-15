package com.example.socialmedia

object GLOBALS {
    private const val SERVER_PROTOCOL: String = "http"
    private const val SERVER_ADDRESS: String = "192.168.5.128"
    private const val SERVER_PORT: Int = 5000

    const val SERVER = "$SERVER_PROTOCOL://$SERVER_ADDRESS:$SERVER_PORT"

    private const val SERVER_ROUTE_LOG_IN_SIGN_UP = "$SERVER/LogIn-SignUp"
    const val SERVER_LOG_IN = "$SERVER_ROUTE_LOG_IN_SIGN_UP/userLogIn"
    const val SERVER_SIGN_UP = "$SERVER_ROUTE_LOG_IN_SIGN_UP/userSignUp"
    const val SERVER_USER_EXIST = "$SERVER_ROUTE_LOG_IN_SIGN_UP/userExist"

    const val SERVER_PROFILE = "$SERVER/profile/fromID"
    fun SERVER_PROFILE_PIC(id: Long): String{
        return "$SERVER/profile/img/$id"
    }

    const val SERVER_PROFILE_POSTS = "$SERVER/profile/posts"

    const val KEY_SIGNIN = "signIn"
    const val SIGN_IN_FAILED = "failed"

    const val SHARED_PREF_ID_USER = "user"
    const val SP_KEY_ID = "ID"
}