package com.tubes.gapedulidilindungi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tubes.gapedulidilindungi.R
import com.tubes.gapedulidilindungi.data.BookmarkData
import com.tubes.gapedulidilindungi.data.BookmarkViewModel
import kotlinx.android.synthetic.main.fragment_faskes_details.view.*

class FaskesDetailsFragment : Fragment() {

    private lateinit var mBookmarkViewModel: BookmarkViewModel
//    private lateinit var bookmarkList: List<BookmarkData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_faskes_details, container, false)

        mBookmarkViewModel = ViewModelProvider(this).get(BookmarkViewModel::class.java)
//        mBookmarkViewModel.readAllData.observe(viewLifecycleOwner, Observer { bookmark ->
//            bookmarkList = bookmark
//        })

        val id = arguments?.getInt("id")
        view.textViewFaskes__titledetail.setText(arguments?.getString("nama_faskes"))
        view.textViewFaskes__kodedetail.setText(arguments?.getString("kode_faskes"))
        view.textViewFaskes__jenisdetail.setText(arguments?.getString("jenis_faskes"))
        view.textViewFaskes__alamatdetail.setText(arguments?.getString("alamat_faskes"))
        view.textViewFaskes__notelpdetail.setText(arguments?.getString("notelp_faskes"))
        val status = arguments?.getString("status_faskes")
        view.textViewFaskes__statusdesc.setText(status)
        if (status == "Siap Vaksinasi") {
            view.imageViewFaskes__statusimage.setImageResource(R.drawable.ready)
        }
        else {
            view.imageViewFaskes__statusimage.setImageResource(R.drawable.not_ready)
        }

//        val isExist = mBookmarkViewModel.isBookmarkExist(id)
//        if (isExist) {
//            view.
//        }

//        if (bookmarkList.any { b -> b.id == id }) {
//            view.button__bookmark.setText("+ Bookmark")
//        }
//        else {
//            view.button__bookmark.setText("- Unbookmark")
//        }


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val searchFragment = SearchFragment()
                val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                transaction.replace(R.id.fragment_container, searchFragment)
                transaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        // Inflate the layout for this fragment
        return view
    }
}