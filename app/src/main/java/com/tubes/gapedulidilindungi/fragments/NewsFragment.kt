package com.tubes.gapedulidilindungi.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.tubes.gapedulidilindungi.*
import com.tubes.gapedulidilindungi.models.NewsModel
import com.tubes.gapedulidilindungi.retrofit.ApiService
import kotlinx.android.synthetic.main.fragment_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {
    private val TAG: String = "NewsActivity"

    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
        getNewsDataFromApi()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(arrayListOf(), object : NewsAdapter.OnAdapterListener {
            override fun onClick(result: NewsModel.Results) {
                val newsDetailsFragment = NewsDetailsFragment()
                val bundle = Bundle()
                bundle.putString("news_url", result.link[0])
                newsDetailsFragment.arguments = bundle

                if (newsDetailsFragment != null) {
                    val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                    transaction.replace(R.id.fragment_container, newsDetailsFragment)
                    transaction.commit()
                }
            }

        })
        recyclerViewNews__newsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun getNewsDataFromApi() {
        progressBarNews__newsLoading.visibility = View.VISIBLE
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        with(ApiService) {
            endpoint.getNews()
                .enqueue(object : Callback<NewsModel> {
                    override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                        progressBarNews__newsLoading.visibility = View.GONE
                        activity?.window?.clearFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        printLog( ">>> onFailure <<< : $t")
                    }
                    override fun onResponse(
                        call: Call<NewsModel>,
                        response: Response<NewsModel>
                    ) {
                        progressBarNews__newsLoading.visibility = View.GONE
                        activity?.window?.clearFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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