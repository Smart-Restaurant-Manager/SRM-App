package com.srm.srmapp.ui.repice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.srm.srmapp.R
import com.srm.srmapp.databinding.FragmentReceiptMainBinding
import com.srm.srmapp.databinding.FragmentStockMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RecipeMainFragment : Fragment() {
    private lateinit var binding: FragmentReceiptMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentReceiptMainBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.btback.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}