package com.example.socialmedia

object GLOBALS {
    private const val SERVER_PROTOCOL: String = "http"
    private const val SERVER_ADDRESS: String = "192.168.1.102"
    private const val SERVER_PORT: Int = 5000

    const val SERVER = "$SERVER_PROTOCOL://$SERVER_ADDRESS:$SERVER_PORT"

    // log in sign up
    private const val SERVER_ROUTE_LOG_IN_SIGN_UP = "$SERVER/LogIn-SignUp"
    const val SERVER_LOG_IN = "$SERVER_ROUTE_LOG_IN_SIGN_UP/userLogIn"
    const val SERVER_SIGN_UP = "$SERVER_ROUTE_LOG_IN_SIGN_UP/userSignUp"
    const val SERVER_USER_EXIST = "$SERVER_ROUTE_LOG_IN_SIGN_UP/userExist"

    // profile
    private const val SERVER_PROFILE_ROUTE: String = "$SERVER/profile"
    const val SERVER_PROFILE : String = "$SERVER_PROFILE_ROUTE/fromID"
    fun SERVER_PROFILE_PIC(id: Long) : String = "$SERVER_PROFILE_ROUTE/img/$id"


    //data
    private const val SERVER_DATA_ROUTE: String = "$SERVER/data"
    const val CONTENT_POST: String = "post"
    const val CONTENT_SHEET: String = "sheet"
    const val CONTENT_SHOP: String = "shop"

    //preview
    fun GET_IMG_PREVIEW(route:String,id: Long) : String{
        if(route != CONTENT_POST && route != CONTENT_SHEET && route != CONTENT_SHOP) return ""
        else return "$SERVER_DATA_ROUTE/$route/$id"
    }

    //shared routes (post,sheet,shop)
    fun LOAD_DATA(route: String) : String = "$SERVER_DATA_ROUTE/$route/load"
    fun UPDATE_DATA(route: String) : String = "$SERVER_DATA_ROUTE/$route/update"
    fun DELETE_DATA(route: String) : String = "$SERVER_DATA_ROUTE/$route/delete"
    fun INFO_DATA(route: String) : String = "$SERVER_DATA_ROUTE/$route/info"


    const val KEY_SIGNIN : String = "signIn"

    const val SHARED_PREF_ID_USER : String = "user"
    const val SP_KEY_ID : String = "ID"
    const val SP_KEY_PW : String = "PW"

    const val CAMERA_PHOTO_RESULT_CODE: Int = 102
    const val CAMERA_PERMISSION_REQUEST_CODE: Int = 101
    const val LOCATION_PERMISSION_CODE: Int = 104
    const val IMAGE_REQUEST_CODE: Int = 103
}