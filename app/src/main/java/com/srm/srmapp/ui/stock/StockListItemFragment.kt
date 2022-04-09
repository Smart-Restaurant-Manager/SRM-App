package com.srm.srmapp.ui.stock

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Stock
import com.srm.srmapp.databinding.FragmentStockListItemBinding
import com.srm.srmapp.databinding.RvItemStockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockListItemFragment : Fragment() {
    private lateinit var binding: FragmentStockListItemBinding
    private lateinit var adapter: Adapter<RvItemStockBinding, Stock>
    private lateinit var plotView: PlotView
    private val viewmodel by activityViewModels<StockViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStockListItemBinding.inflate(inflater, container, false)
        plotView = binding.pvPlot
        val food = arguments?.getParcelable<Food>("item")
        val title = arguments?.getString("title")
        if (food != null && title != null) {
            setupView(food, title)
            setupObservables()
        }
        return binding.root
    }

    private fun setupObservables() {
        viewmodel.getStockListLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> TODO()
                is Resource.Loading -> {
                    binding.srStockRefresh.isRefreshing = true
                }
                is Resource.Success -> {
                    binding.srStockRefresh.isRefreshing = false
                    val data = it.data
                    if (data != null) {
                        adapter.updateItems(data)
                        plotView.apply {
                            setDate(data.map { it.quantity }, data.map { it.quantity.toString() })
                            setOnDownListener { idx ->
                                val item = data[idx]
                                AlertDialog.Builder(requireContext()).apply {
                                    setMessage("${item.id} ${item.expirationDate} ${item.quantity}")
                                    show()
                                }
                            }
                        }
                    }
                }
                else -> {}
            }
        }
        if (viewmodel.getStockListLiveData().value == null)
            viewmodel.refreshStockList()
    }

    private fun setupView(food: Food, title: String) {
        binding.tvTitle.text = title
        adapter = Adapter(emptyList(), R.layout.rv_item_stock, { view ->
            RvItemStockBinding.bind(view)
        }, { itemBinding, item -> // seyup child views
            itemBinding.apply {
                tvName.text = "${food.name} ${item.id}"
                tvQuantity.text = "${item.quantity} ${food.units}"
                tvCaducidad.text = item.expirationDate.toString()
                tvLote.visibility = View.GONE
            }
        })
        binding.rvHistory.adapter = adapter
        binding.rvHistory.layoutManager = LinearLayoutManager(activity)
        binding.srStockRefresh.setOnRefreshListener {
            viewmodel.refreshStockList(/* TODO ADD type */)
        }
        binding.srStockRefresh.isEnabled = true
        binding.btback.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}