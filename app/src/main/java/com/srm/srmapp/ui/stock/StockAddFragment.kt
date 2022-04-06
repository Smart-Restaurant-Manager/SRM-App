package com.srm.srmapp.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.databinding.FragmentStockAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockAddFragment : Fragment() {
    private lateinit var binding: FragmentStockAddBinding
    private val viewmodel by viewModels<StockViewmodel>()
    private lateinit var argFoodType: Food.FoodType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStockAddBinding.inflate(inflater, container, false)
        argFoodType = arguments?.getSerializable("id") as Food.FoodType
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.btAdd.setOnClickListener {
            binding.apply {
                val name = edName.text.toString()
                val quantity = edQuantity.text.toString()
                val date = cvFecha.date.toString()
                val units = "TODO"
                viewmodel.addFood(Food(argFoodType, -1, name, units))
            }
            findNavController().popBackStack()
        }
    }
}