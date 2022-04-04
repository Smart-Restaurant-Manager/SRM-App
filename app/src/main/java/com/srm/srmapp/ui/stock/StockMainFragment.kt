package com.srm.srmapp.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.srm.srmapp.databinding.FragmentStockMainBinding

class StockMainFragment : Fragment() {
    private lateinit var binding: FragmentStockMainBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStockMainBinding.inflate(inflater, container, false)
        return binding.root
    }


}