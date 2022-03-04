package com.tubes.gapedulidilindungi.models

import com.google.gson.annotations.SerializedName

data class CheckinRequestModel(
    @SerializedName("qrCode") var qrCode: String? = null,
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("longitude") var longitude: Double? = null
)