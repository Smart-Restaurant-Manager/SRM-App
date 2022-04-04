package com.srm.srmapp.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.srm.srmapp.databinding.FragmentStockListBinding

class StockListFragment : Fragment() {
    private lateinit var binding: FragmentStockListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStockListBinding.inflate(inflater, container, false)
        return binding.root
    }
}