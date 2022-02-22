package com.tubes.gapedulidilindungi.retrofit

import com.tubes.gapedulidilindungi.NewsModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiEndpoint {
    @GET("get-news")
    fun getNews():Call<NewsModel>
}