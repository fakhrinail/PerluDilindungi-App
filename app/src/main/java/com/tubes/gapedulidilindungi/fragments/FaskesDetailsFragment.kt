package com.tubes.gapedulidilindungi.fragments

import android.content.Intent
import android.net.Uri
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
//    private var bookmarkList: List<BookmarkData> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_faskes_details, container, false)

//        bookmarkList = mBookmarkViewModel.readAllData.value!!

        val id = arguments?.getInt("id")
        val nama = arguments?.getString("nama_faskes")
        val kode = arguments?.getString("kode_faskes")
        val jenis = arguments?.getString("jenis_faskes")
        val alamat = arguments?.getString("alamat_faskes")
        val notelp = arguments?.getString("notelp_faskes")
        val status = arguments?.getString("status_faskes")
        view.textViewFaskes__titledetail.setText(nama)
        view.textViewFaskes__kodedetail.setText("Kode: " + kode)
        view.textViewFaskes__jenisdetail.setText(jenis)
        view.textViewFaskes__alamatdetail.setText(alamat)
        view.textViewFaskes__notelpdetail.setText(notelp)
        view.textViewFaskes__statusdesc.setText(status)
        if (status == "Siap Vaksinasi") {
            view.imageViewFaskes__statusimage.setImageResource(R.drawable.ready)
        }
        else {
            view.imageViewFaskes__statusimage.setImageResource(R.drawable.not_ready)
        }

        view.button__gmaps.setOnClickListener {
            // Create a Uri from an intent string. Use the result to create an Intent.
            val gmmIntentUri = Uri.parse("geo:0,0?q=" + alamat)

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps")

            // Attempt to start an activity that can handle the Intent
            startActivity(mapIntent)
        }

        mBookmarkViewModel = ViewModelProvider(this).get(BookmarkViewModel::class.java)
        mBookmarkViewModel.readAllData.observe(viewLifecycleOwner, Observer { bookmark ->
            if (bookmark.any{ b -> b.id == id }) {
                view.button__bookmark.setText("- Unbookmark")
                view.button__bookmark.setOnClickListener {
                    mBookmarkViewModel.deleteBookmark(id!!)
                    view.button__bookmark.setText("+ Bookmark")
                }
            }
            else {
                view.button__bookmark.setText("+ Bookmark")
                view.button__bookmark.setOnClickListener {
                    val bookmark = BookmarkData(
                        id!!,
                        kode!!,
                        nama,
                        alamat,
                        notelp,
                        jenis,
                        status
                    )

                    mBookmarkViewModel.addBookmark(bookmark)
                    view.button__bookmark.setText("- Unbookmark")
                }
            }
        })

//        view.button__bookmark.setText(bookmarkList.any { b -> b.id == id }.toString())
//        view.button__bookmark.setText(bookmarkList.size.toString())

//        view.button__bookmark.setText("+ Bookmark")


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