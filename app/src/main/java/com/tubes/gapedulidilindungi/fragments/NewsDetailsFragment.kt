package com.tubes.gapedulidilindungi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tubes.gapedulidilindungi.R
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_news_details.*

class NewsDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val newsFragment = NewsFragment()
                val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                transaction.replace(R.id.fragment_container, newsFragment)
                transaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        val url = arguments?.getString("news_url")
        if (url != null) {
            initiateWebView(url)
        }
    }

    private fun initiateWebView(url: String = "https://google.com/") {
        webViewNewsDetails__article.webViewClient = WebViewClient()
        webViewNewsDetails__article.apply {
            loadUrl(url)
            settings.javaScriptEnabled = true
            settings.allowContentAccess = true
            settings.domStorageEnabled = true
//            settings.safeBrowsingEnabled = true
        }
    }
}