package com.srm.srmapp.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Stock
import com.srm.srmapp.databinding.FragmentStockListItemBinding
import com.srm.srmapp.databinding.RvItemStockBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class StockListItemFragment : Fragment() {
    private lateinit var binding: FragmentStockListItemBinding
    private val viewmodel by viewModels<StockViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStockListItemBinding.inflate(inflater, container, false)
        val food = arguments?.getParcelable<Food>("item")
        val title = arguments?.getString("title")
        if (food != null && title != null)
            setupView(food, title)
        return binding.root
    }

    private fun setupView(food: Food, title: String) {
        binding.tvTitle.text = title
        binding.rvHistory.adapter = Adapter(mutableListOf(
            Stock(1, 32.0f, Calendar.getInstance().time),
            Stock(4, 23.0f, Calendar.getInstance().time),
            Stock(2, 22.0f, Calendar.getInstance().time),
            Stock(11, 23.0f, Calendar.getInstance().time),
        ), { view ->
            RvItemStockBinding.bind(view)
        }, { itemBinding, item -> // seyup child views
            itemBinding.apply {
                tvName.text = "${food.name} ${item.id}"
                tvQuantity.text = item.quantity.toString()
                tvCaducidad.text = item.expirationDate.toString()
            }
        })
        binding.rvHistory.layoutManager = LinearLayoutManager(activity)



    }
}