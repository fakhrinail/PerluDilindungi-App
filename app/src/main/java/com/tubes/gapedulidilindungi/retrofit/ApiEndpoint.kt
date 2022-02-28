package com.tubes.gapedulidilindungi.retrofit

import com.tubes.gapedulidilindungi.FaskesDetail
import com.tubes.gapedulidilindungi.NewsModel
import com.tubes.gapedulidilindungi.ProvinceCityModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndpoint {
    @GET("api/get-news")
    fun getNews():Call<NewsModel>

    @GET("get-province")
    fun getProvinces():Call<ProvinceCityModel>

    @GET("get-city")
    fun getCities():Call<ProvinceCityModel>

    @GET("get-faskes-vaksinasi")
    fun getFaskes(@Query("province") province: String, @Query("city") city: String):Call<FaskesDetail>
}