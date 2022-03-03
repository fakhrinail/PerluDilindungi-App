package com.tubes.gapedulidilindungi.retrofit

import com.tubes.gapedulidilindungi.models.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiEndpoint {
    @GET("api/get-news")
    fun getNews(): Call<NewsModel>

    @GET("api/get-province")
    fun getProvinces():Call<ProvinceCityModel>

    @GET("api/get-city")
    fun getCities(@Query("start_id") provinceId: String):Call<ProvinceCityModel>

    @GET("api/get-faskes-vaksinasi")
    fun getFaskes(@Query("province") province: String, @Query("city") city: String):Call<FaskesModel>

    @Headers("Content-Type: application/json")
    @POST("check-in")
    fun checkin(@Body checkinData: CheckinRequestModel): Call<CheckinResponseModel>
}