package com.tubes.gapedulidilindungi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tubes.gapedulidilindungi.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {
    private val TAG: String = "NewsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
    }

    override fun onStart() {
        super.onStart()
        getNewsDataFromApi()
    }

    private fun getNewsDataFromApi() {
        with(ApiService) {
            endpoint.getNews()
                .enqueue(object : Callback<NewsModel> {
                    override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                        printLog( t.toString() )
                    }
                    override fun onResponse(
                        call: Call<NewsModel>,
                        response: Response<NewsModel>
                    ) {
                        if (response.isSuccessful) {
                            printLog( response.body().toString() )
                        }
                    }
                })
        }
    }

    private fun printLog(message: String) {
        Log.d(TAG, message)
    }

}