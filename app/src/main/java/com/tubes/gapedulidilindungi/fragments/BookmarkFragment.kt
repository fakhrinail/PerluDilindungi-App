package com.tubes.gapedulidilindungi.fragments

import android.os.Bundle
import android.util.Log
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
import com.tubes.gapedulidilindungi.data.ListBookmarkData
import kotlinx.android.synthetic.main.fragment_bookmark.*
import kotlinx.android.synthetic.main.fragment_bookmark.view.*

class BookmarkFragment : Fragment() {

    companion object {
        const val LIST_FASKES = "LIST_FASKES"
    }

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
                bundle.putInt("id", result.id)
                bundle.putString("kode_faskes", result.kodeFaskes)
                bundle.putString("nama_faskes", result.namaFaskes)
                bundle.putString("alamat_faskes", result.alamatFaskes)
                bundle.putString("notelp_faskes", result.noTelpFaskes)
                bundle.putString("jenis_faskes", result.jenisFaskes)
                bundle.putString("status_faskes", result.statusFaskes)
                faskesDetailsFragment.arguments = bundle

//                val fragment = parentFragmentManager.findFragmentById(R.id.fragment_container)
//                if (fragment != null) {
//                    parentFragmentManager.beginTransaction().remove(fragment).commit()
//                }
//                parentFragmentManager.beginTransaction().apply {
//                    add(R.id.fragment_container, faskesDetailsFragment)
//                    commit()
//                }

//                if (faskesDetailsFragment != null) {
//                    val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
//                    transaction.replace(R.id.fragment_container, faskesDetailsFragment)
//                    transaction.commit()
//                }
                
            }

        })

        val bundle = arguments
        Log.i("BISMILLAH", arguments.toString())
        if (bundle != null) {
            val data = bundle.getParcelable<ListBookmarkData>(LIST_FASKES)
            Log.i("BISMILLAH", data?.listFaskes.toString())
            data?.listFaskes?.let { faskesAdapter.setData(it) }
        } else {
            mBookmarkViewModel = ViewModelProvider(this).get(BookmarkViewModel::class.java)
            mBookmarkViewModel.readAllData.observe(viewLifecycleOwner, Observer { bookmark ->
                faskesAdapter.setData(bookmark)
            })
        }

        recyclerViewFaskes__faskesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = faskesAdapter
        }
    }
}