package com.srm.srmapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.srm.srmapp.Utils
import com.srm.srmapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this)
        }
        setupView()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this)
        }
    }

    override fun onDestroy() {
        Timber.d("Destroyed")
        super.onDestroy()
    }
}