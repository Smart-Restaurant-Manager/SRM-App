package com.srm.srmapp.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Stock
import com.srm.srmapp.databinding.FragmentStockListItemBinding
import java.util.*

class StockListItemFragment : Fragment() {
    private lateinit var binding: FragmentStockListItemBinding
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
            Stock(1, 32.0f, Calendar.getInstance().time, 1),
            Stock(4, 23.0f, Calendar.getInstance().time, 4),
            Stock(2, 22.0f, Calendar.getInstance().time, 2),
            Stock(11, 23.0f, Calendar.getInstance().time, 3),
        ), { itemBinding, item -> // seyup child views
            itemBinding.apply {
                tvName.text = "${food.name} ${item.id}"
                tvLote.text = item.lote.toString()
                tvQuantity.text = item.quantity.toString()
                tvCaducidad.text = item.expirationDate.toString()
            }
        })
        binding.rvHistory.layoutManager = LinearLayoutManager(activity)
    }
}