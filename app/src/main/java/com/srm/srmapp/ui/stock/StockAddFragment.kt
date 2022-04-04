package com.srm.srmapp.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.srm.srmapp.R
import com.srm.srmapp.databinding.FragmentStockAddBinding

class StockAddFragment : Fragment() {
    private lateinit var binding: FragmentStockAddBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStockAddBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.btAdd.setOnClickListener {
            // call viewmodel add
            findNavController().popBackStack()
        }
    }
}