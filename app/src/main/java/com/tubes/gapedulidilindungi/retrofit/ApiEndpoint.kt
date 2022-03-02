package com.tubes.gapedulidilindungi.retrofit

import com.tubes.gapedulidilindungi.FaskesDetail
import com.tubes.gapedulidilindungi.FaskesModel
import com.tubes.gapedulidilindungi.NewsModel
import com.tubes.gapedulidilindungi.ProvinceCityModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndpoint {
    @GET("api/get-news")
    fun getNews():Call<NewsModel>

    @GET("api/get-province")
    fun getProvinces():Call<ProvinceCityModel>

    @GET("api/get-city")
    fun getCities(@Query("start_id") provinceId: String):Call<ProvinceCityModel>

    @GET("api/get-faskes-vaksinasi")
    fun getFaskes(@Query("province") province: String, @Query("city") city: String):Call<FaskesModel>
}