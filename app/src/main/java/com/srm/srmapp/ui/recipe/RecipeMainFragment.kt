package com.srm.srmapp.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.srm.srmapp.R
import com.srm.srmapp.databinding.FragmentReceiptMainBinding
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
        val bts = binding.btEntrantes as ViewGroup
        for (i in 0 until bts.childCount) {
            val v = bts.getChildAt(i)
            if (v is Button) {
                Timber.d("${v.text}")
                v.setOnClickListener {
                    val arg = Bundle()
                    arg.putInt("id", it.id)
                    arg.putString("title", (it as Button).text.toString())
                    findNavController().navigate(R.id.action_receiptMainFragment_to_menuCardList, args = arg)
                }
            }
        }


        binding.btback.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}