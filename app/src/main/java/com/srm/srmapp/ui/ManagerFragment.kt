package com.srm.srmapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.srm.srmapp.R
import com.srm.srmapp.databinding.FragmentManagerBinding

class ManagerFragment : Fragment() {
    private lateinit var binding: FragmentManagerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentManagerBinding.inflate(inflater, container, false)
        binding.btStock.setOnClickListener {
            findNavController().navigate(R.id.action_managerFragment_to_stockMainFragment)
        }
        binding.btMenu.setOnClickListener {
            findNavController().navigate(R.id.action_managerFragment_to_receiptMainFragment)
        }
        return binding.root
    }
}