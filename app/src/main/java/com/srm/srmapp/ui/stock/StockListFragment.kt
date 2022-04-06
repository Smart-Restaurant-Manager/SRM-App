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
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.databinding.FragmentStockListBinding
import com.srm.srmapp.databinding.RvItemStockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockListFragment : Fragment() {
    private lateinit var binding: FragmentStockListBinding
    private val viewmodel by viewModels<StockViewmodel>()
    private lateinit var adapter: Adapter<RvItemStockBinding, Food>
    private lateinit var argTitle: String
    private lateinit var argFoodType: Food.FoodType
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStockListBinding.inflate(inflater, container, false)
        argTitle = arguments?.getString("title") ?: "ERROR"
        argFoodType = Food.parseId(arguments?.getInt("id") ?: -1)
        setupView(argTitle, argFoodType)
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        viewmodel.refreshFoodList()
        viewmodel.getFoodListLiveData().observe(requireActivity()) {
            when (it) {
                is Resource.Error -> TODO()
                is Resource.Loading -> {
                    binding.srFoodRefresh.isRefreshing = true;
                }
                is Resource.Success -> {
                    binding.srFoodRefresh.isRefreshing = false;
                    it.data?.let { list ->
                        adapter.updateItems(list)
                    }
                }
                else -> {}
            }
        }
    }

    private fun setupView(title: String, id: Food.FoodType) {
        binding.tvStockTitle.text = title
        adapter = Adapter(emptyList(), R.layout.rv_item_stock, { view ->
            RvItemStockBinding.bind(view)
        }, { itemBinding, item -> // setup child views
            itemBinding.apply {
                tvLote.text = item.name
                tvQuantity.text = item.units
                tvName.text = item.name
                tvCaducidad.visibility = View.GONE
            }
        }) { view, item -> // on item click
            val arg = Bundle()
            arg.putParcelable("item", item)
            arg.putString("title", title)
            findNavController().navigate(R.id.action_stockListFragment_to_stockListItemFragment, arg)
        }
        binding.rvItems.adapter = adapter
        binding.rvItems.layoutManager = LinearLayoutManager(activity)
        binding.btAdd.setOnClickListener {
            findNavController().navigate(R.id.action_stockListFragment_to_stockAddFragment, Bundle().let {
                it.putSerializable("type", id)
                it
            })
        }
        binding.srFoodRefresh.setOnRefreshListener {
            viewmodel.refreshFoodList(/* TODO ADD Type */)
        }
        binding.srFoodRefresh.isEnabled = true
    }
}