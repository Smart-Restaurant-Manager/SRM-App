package com.srm.srmapp.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.srm.srmapp.R
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.databinding.FragmentStockListBinding
import com.srm.srmapp.databinding.RvItemStockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockListFragment : Fragment() {
    private lateinit var binding: FragmentStockListBinding
    private val viewmodel by viewModels<StockViewmodel>()
    private val validIds = arrayListOf(R.id.btCarne, R.id.btCereales,
        R.id.btMariscos, R.id.btEspecias,
        R.id.btVegetales, R.id.btLacteos)
    private lateinit var adapter: Adapter<RvItemStockBinding, Food>
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
        adapter = Adapter(emptyList(), { view ->
            RvItemStockBinding.bind(view)
        }, { itemBinding, item -> // setup child views
            itemBinding.apply {
                tvLote.text = item.name
                tvQuantity.text = item.units
                tvName.text = item.name
                tvCaducidad.text = ""
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

        binding.rvItems.adapter = adapter
        viewmodel.getFoodListLiveData().observe(requireActivity()) {
            val list = it.data
            if (list != null) {
                adapter.updateItems(list)
            }
        }

        viewmodel.refreshFoodList()
    }
}