package com.srm.srmapp.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.srm.srmapp.R
import com.srm.srmapp.ui.MainActivity

class ReportFragment : Fragment(), MainActivity.FragmentName {

    companion object {
        fun newInstance() = ReportFragment()
    }

    private lateinit var viewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.report_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ReportViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun getName(): String {
        return "Report"
    }
}