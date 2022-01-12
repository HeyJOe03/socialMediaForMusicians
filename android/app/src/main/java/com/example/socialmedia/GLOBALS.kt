package com.example.socialmedia

object GLOBALS {
    private const val SERVER_PROTOCOL: String = "http"
    private const val SERVER_ADDRESS: String = "192.168.1.102"
    private const val SERVER_PORT: Int = 5000

    const val SERVER = "$SERVER_PROTOCOL://$SERVER_ADDRESS:$SERVER_PORT"

    private const val SERVER_ROUTE_LOG_IN_SIGN_UP = "$SERVER/LogIn-SignUp"
    const val SERVER_LOG_IN = "$SERVER_ROUTE_LOG_IN_SIGN_UP/userLogIn"
    const val SERVER_SIGN_UP = "$SERVER_ROUTE_LOG_IN_SIGN_UP/userSignUp"
    const val SERVER_USER_EXIST = "$SERVER_ROUTE_LOG_IN_SIGN_UP/userExist"

    const val SERVER_PROFILE = "$SERVER/profile/fromID"

    fun SERVER_PROFILE_PIC(id: Long) = "$SERVER/profile/img/$id"

    //const val SERVER_PROFILE_POSTS = "$SERVER/profile/posts"

    const val KEY_SIGNIN = "signIn"

    const val SHARED_PREF_ID_USER = "user"
    const val SP_KEY_ID = "ID"
    const val SP_KEY_PW = "PW"

    const val CAMERA_PHOTO_RESULT_CODE: Int = 102
    const val CAMERA_PERMISSION_REQUEST_CODE: Int = 101
    const val LOCATION_PERMISSION_CODE: Int = 104
    const val IMAGE_REQUEST_CODE: Int = 103
}