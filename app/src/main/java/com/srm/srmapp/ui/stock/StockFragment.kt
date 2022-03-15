package com.srm.srmapp.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.srm.srmapp.R
import com.srm.srmapp.ui.MainActivity

class StockFragment : Fragment(), MainActivity.FragmentName {
    companion object {
        fun newInstance() = StockFragment()
    }

    private lateinit var viewModel: StockViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.stock_fragment, container, false)
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