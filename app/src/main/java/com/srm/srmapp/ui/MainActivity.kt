package com.srm.srmapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.srm.srmapp.Utils
import com.srm.srmapp.databinding.ActivityMainBinding
import com.srm.srmapp.ui.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setupViewpager(binding.pager)
        setContentView(binding.root)
        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this)
        }
    }


    interface FragmentName {
        fun getName(): String
    }

    private fun setupViewpager(pager: ViewPager2) {
        Timber.d("Setup Viewpager")

        val fragmentArray = arrayOf(
            LoginFragment(),
        )

//        pager.offscreenPageLimit = fragmentArray.size / 2 + 1
        pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragmentArray.size
            override fun createFragment(position: Int): Fragment {
                return fragmentArray[position]
            }
        }

        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this)
        }
    }

}