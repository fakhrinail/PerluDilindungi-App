package com.tubes.gapedulidilindungi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tubes.gapedulidilindungi.retrofit.ApiService
import kotlinx.android.synthetic.main.activity_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {
    private val TAG: String = "NewsActivity"

    lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
        getNewsDataFromApi()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(arrayListOf(), object : NewsAdapter.OnAdapterListener {
            override fun onClick(result: NewsModel.Results) {
                val intent = Intent(this@NewsActivity, NewsDetailsActivity::class.java)
                    .putExtra("news_url", result.link[0])
                startActivity(intent)
            }

        })
        recyclerViewNews__newsList.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = newsAdapter
        }
    }

    private fun getNewsDataFromApi() {
        progressBarNews__newsLoading.visibility = View.VISIBLE
        with(ApiService) {
            endpoint.getNews()
                .enqueue(object : Callback<NewsModel> {
                    override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                        progressBarNews__newsLoading.visibility = View.GONE
                        printLog( ">>> onFailure <<< : $t")
                    }
                    override fun onResponse(
                        call: Call<NewsModel>,
                        response: Response<NewsModel>
                    ) {
                        progressBarNews__newsLoading.visibility = View.GONE
                        if (response.isSuccessful) {
                            showData( response.body()!! )
                        }
                    }
                })
        }
    }

    private fun printLog(message: String) {
        Log.d(TAG, message)
    }

    private fun showData (data: NewsModel) {
        val results = data.results
        newsAdapter.setData(results)
//        for (result in results) {
//            printLog("title: ${result.title}")
//        }
    }

}