package com.tubes.gapedulidilindungi.models

import com.google.gson.annotations.SerializedName

data class CheckinResponseModel(
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : UserInfo?    = UserInfo()
)

data class UserInfo(
    @SerializedName("userStatus" ) var userStatus : String? = null,
    @SerializedName("reason"     ) var reason     : String? = null
)