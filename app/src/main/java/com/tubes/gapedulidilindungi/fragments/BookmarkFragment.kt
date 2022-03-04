package com.tubes.gapedulidilindungi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tubes.gapedulidilindungi.FaskesAdapter
import com.tubes.gapedulidilindungi.NewsAdapter
import com.tubes.gapedulidilindungi.R
import com.tubes.gapedulidilindungi.data.BookmarkData
import com.tubes.gapedulidilindungi.data.BookmarkViewModel
import com.tubes.gapedulidilindungi.models.NewsModel
import com.tubes.gapedulidilindungi.retrofit.ApiService
import kotlinx.android.synthetic.main.fragment_bookmark.*
import kotlinx.android.synthetic.main.fragment_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarkFragment : Fragment() {

    private lateinit var mBookmarkViewModel: BookmarkViewModel

    lateinit var faskesAdapter: FaskesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        faskesAdapter = FaskesAdapter(arrayListOf(), object : FaskesAdapter.OnAdapterListener {
            override fun onClick(result: BookmarkData) {
                val faskesDetailsFragment = FaskesDetailsFragment()
                val bundle = Bundle()
                bundle.putString("kode_faskes", result.kodeFaskes)
                bundle.putString("nama_faskes", result.namaFaskes)
                bundle.putString("alamat_faskes", result.alamatFaskes)
                bundle.putString("notelp_faskes", result.noTelpFaskes)
                bundle.putString("jenis_faskes", result.jenisFaskes)
                bundle.putString("status_faskes", result.statusFaskes)
                faskesDetailsFragment.arguments = bundle

                if (faskesDetailsFragment != null) {
                    val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                    transaction.replace(R.id.fragment_container, faskesDetailsFragment)
                    transaction.commit()
                }
            }

        })
        recyclerViewFaskes__faskesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = faskesAdapter
        }

//        mBookmarkViewModel = ViewModelProvider(this).get(BookmarkViewModel::class.java)
//        mBookmarkViewModel.readAllData.observe(viewLifecycleOwner, Observer { bookmark ->
//            faskesAdapter.setData(bookmark)
//        })
    }
}