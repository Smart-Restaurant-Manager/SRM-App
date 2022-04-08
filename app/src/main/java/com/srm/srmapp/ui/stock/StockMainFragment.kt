package com.srm.srmapp.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.srm.srmapp.R
import com.srm.srmapp.databinding.FragmentStockMainBinding
import timber.log.Timber

class StockMainFragment : Fragment() {
    private lateinit var binding: FragmentStockMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStockMainBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        val bts = binding.btCarne.parent as ViewGroup
        for (i in 0 until bts.childCount) {
            val v = bts.getChildAt(i)
            if (v is Button) {
                Timber.d("${v.text}")
                v.setOnClickListener {
                    val arg = Bundle()
                    arg.putInt("id", it.id)
                    arg.putString("title", (it as Button).text.toString())
                    findNavController().navigate(R.id.action_stockMainFragment_to_stockListFragment, args = arg)
                }
            }
        }
        binding.btback.setOnClickListener {
            findNavController().navigate(R.id.action_stockMainFragment_to_managerFragment)
        }
    }
}