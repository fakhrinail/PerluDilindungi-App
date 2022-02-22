package com.tubes.gapedulidilindungi.retrofit

import retrofit2.http.GET

interface ApiEndpoint {
    @GET("get-news")
    fun getNews()
}