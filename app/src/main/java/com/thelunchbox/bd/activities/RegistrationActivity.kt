package com.thelunchbox.bd.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.thelunchbox.bd.databinding.ActivityLoginBinding
import com.thelunchbox.bd.databinding.ActivityRegistrationBinding
import com.thelunchbox.bd.fragments.BuyerRegistrationFragment
import com.thelunchbox.bd.fragments.CookerRegistrationFragment
import com.thelunchbox.bd.utils.ViewPagerAdapter

class RegistrationActivity : AppCompatActivity() {

    private var _binding: ActivityRegistrationBinding? = null
    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)

        setupViewPager(_binding!!.viewpager)
        _binding!!.tabs.setupWithViewPager(_binding!!.viewpager)

        _binding!!.back.setOnClickListener {
            finish()
        }

    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(BuyerRegistrationFragment(), "Buyer")
        adapter.addFragment(CookerRegistrationFragment(), "Cooker")
        viewPager.setAdapter(adapter)
    }

}