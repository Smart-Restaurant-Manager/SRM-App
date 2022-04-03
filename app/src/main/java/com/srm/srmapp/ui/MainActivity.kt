package com.srm.srmapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.srm.srmapp.R
import com.srm.srmapp.Utils
import com.srm.srmapp.databinding.ActivityMainBinding
import com.srm.srmapp.ui.booking.BookingFragment
import com.srm.srmapp.ui.login.LoginFragment
import com.srm.srmapp.ui.menu.MenuFragment
import com.srm.srmapp.ui.report.ReportFragment
import com.srm.srmapp.ui.stock.StockFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setupViewpager(binding.pager, binding.tabLayout)
        setContentView(binding.root)
        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this)
        }
    }


    interface FragmentName {
        fun getName(): String
    }

    private fun setupViewpager(pager: ViewPager2, tabLayout: TabLayout) {
        Timber.d("Setup Viewpager")

        val fragmentArray = arrayOf(
            LoginFragment(),
            BookingFragment(),
            MenuFragment(),
            ReportFragment(),
            StockFragment()
        )

        // dont destroy fragments
        pager.offscreenPageLimit = fragmentArray.size / 2 + 1
        pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragmentArray.size
            override fun createFragment(position: Int): Fragment {
                return fragmentArray[position]
            }
        }

        tabLayout.apply {
            TabLayoutMediator(this, pager) { tab, position ->
                Timber.d("Fragment $position ${fragmentArray[position].getName()}")
                tab.text = fragmentArray[position].getName()
            }.attach()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this)
        }
    }

}