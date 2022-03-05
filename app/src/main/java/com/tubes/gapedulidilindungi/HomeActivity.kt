package com.tubes.gapedulidilindungi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.tubes.gapedulidilindungi.fragments.BookmarkFragment
import com.tubes.gapedulidilindungi.fragments.NewsFragment
import com.tubes.gapedulidilindungi.fragments.SearchFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val newsFragment = NewsFragment()
    private val searchFragment = SearchFragment()
    private val bookmarkFragment = BookmarkFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        replaceFragment(newsFragment)

        bottomNavbar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.icNavbar__news -> replaceFragment(newsFragment)
                R.id.icNavbar__location -> replaceFragment(searchFragment)
//                R.id.icNavbar__location -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
//                        searchFragment
//                    )
//                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container_2,
//                        bookmarkFragment
//                    )
//                }
                R.id.icNavbar__bookmark -> {
                    val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                    if (fragment != null) {
                        val childFragment = fragment.childFragmentManager.findFragmentById(R.id.fragment_container_2)
                        if (childFragment !== null) {
                            fragment.childFragmentManager.beginTransaction().remove(childFragment)
                        }
                        supportFragmentManager.beginTransaction().remove(fragment)
                    }
                    replaceFragment(bookmarkFragment)
                }
            }
            true
        }

        btn__qrcodescanner.setOnClickListener {
            val intent = Intent(this@HomeActivity, CheckinActivity::class.java)
            startActivity(intent)
        }
    }

    public fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}