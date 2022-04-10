package com.srm.srmapp.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.srm.srmapp.R
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Stock
import com.srm.srmapp.databinding.FragmentStockAddBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class StockAddFragment : Fragment() {
    private lateinit var binding: FragmentStockAddBinding
    private val viewmodel by viewModels<StockViewmodel>()
    private lateinit var argFoodType: Food.FoodType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStockAddBinding.inflate(inflater, container, false)
        argFoodType = arguments?.getSerializable("type") as Food.FoodType
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.btAdd.setOnClickListener {
            binding.apply {
                val name = edName.text.toString().ifEmpty {
                    Snackbar.make(it, "Invalid Name", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                val quantity = edQuantity.text.toString().ifEmpty {
                    Snackbar.make(it, "Invalid Quantity", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }.toFloat()
                val date = dpFecha.toString().ifEmpty {
                    Snackbar.make(it, "Invalid Date", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                val units = "TODO".toString().ifEmpty { // TODO
                    Snackbar.make(it, "Invalid Unit", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                val food = Food(argFoodType, -1, name, units, listOf(Stock(-1, quantity = quantity, date)))
                viewmodel.addFood(food)
                findNavController().popBackStack()
            }
        }
        binding.btback.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}