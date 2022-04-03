package com.srm.srmapp.ui.stock

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.srm.srmapp.R
import com.srm.srmapp.databinding.StockFragmentBinding
import com.srm.srmapp.ui.MainActivity
import com.srm.srmapp.ui.SecondActivity


class StockFragment : Fragment(), MainActivity.FragmentName {
    companion object {
        fun newInstance() = StockFragment()
    }

    private lateinit var viewModel: StockViewModel
    private lateinit var binding: StockFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = StockFragmentBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.stock1.setOnClickListener{
            activity?.let{
                val intent = Intent (it, SecondActivity::class.java)
                it.startActivity(intent)
            }
        }
        binding.stock2.setOnClickListener{
            activity?.let{
                val intent = Intent (it, SecondActivity::class.java)
                it.startActivity(intent)
            }
        }
        binding.stock3.setOnClickListener{
            activity?.let{
                val intent = Intent (it, SecondActivity::class.java)
                it.startActivity(intent)
            }
        }
        binding.stock4.setOnClickListener{
            activity?.let{
                val intent = Intent (it, SecondActivity::class.java)
                it.startActivity(intent)
            }
        }
        binding.stock5.setOnClickListener{
            activity?.let{
                val intent = Intent (it, SecondActivity::class.java)
                it.startActivity(intent)
            }
        }
        binding.stock6.setOnClickListener{
            activity?.let{
                val intent = Intent (it, SecondActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[StockViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun getName(): String {
        return "Stock"
    }
}

