package com.srm.srmapp.ui.stock

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.srm.srmapp.R
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.FoodType
import com.srm.srmapp.databinding.FragmentStockListBinding

class StockListFragment : Fragment() {
    private lateinit var binding: FragmentStockListBinding

    private val validIds = arrayListOf(R.id.btCarne, R.id.btCereales,
        R.id.btMariscos, R.id.btEspecias,
        R.id.btVegetales, R.id.btLacteos)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStockListBinding.inflate(inflater, container, false)
        val title = arguments?.getString("title")
        val id = arguments?.getInt("id")
        if (title != null && id != null)
            setupView(title, id)
        return binding.root
    }

    private fun setupView(title: String, id: Int) {
        if (id !in validIds)
            return

        binding.tvStockTitle.text = title
        binding.rvItems.adapter = Adapter(listOf(
            Food(FoodType.CARNE, 0, "Name1", "kg"),
            Food(FoodType.CARNE, 1, "Name2", "kg"),
            Food(FoodType.CARNE, 2, "Name3", "kg"),
            Food(FoodType.CARNE, 3, "Name4", "kg"),
            Food(FoodType.CARNE, 4, "Name5", "kg"),
        ), { itemBinding, item -> // setup child views
            itemBinding.apply {
                tvLote.text = "TODO"
                tvQuantity.text = "TODO ${item.units}"
                tvName.text = item.name
                tvCaducidad.text = "TODO"
            }
        }) { view, item -> // on item click
            val arg = Bundle()
            arg.putParcelable("item", item)
            arg.putString("title", title)
            findNavController().navigate(R.id.action_stockListFragment_to_stockListItemFragment, arg)
        }
        binding.rvItems.layoutManager = LinearLayoutManager(activity)

        binding.btAdd.setOnClickListener {
            findNavController().navigate(R.id.action_stockListFragment_to_stockAddFragment)
        }
    }
}