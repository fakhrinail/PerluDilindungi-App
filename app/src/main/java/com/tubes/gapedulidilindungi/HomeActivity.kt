package com.tubes.gapedulidilindungi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tubes.gapedulidilindungi.fragments.BookmarkFragment
import com.tubes.gapedulidilindungi.fragments.LocationFragment
import com.tubes.gapedulidilindungi.fragments.NewsFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val newsFragment = NewsFragment()
    private val locationFragment = LocationFragment()
    private val bookmarkFragment = BookmarkFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        replaceFragment(newsFragment)

        bottomNavbar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.icNavbar__news -> replaceFragment(newsFragment)
                R.id.icNavbar__location -> replaceFragment(locationFragment)
                R.id.icNavbar__bookmark -> replaceFragment(bookmarkFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}