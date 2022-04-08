package com.srm.srmapp.ui.stock

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.databinding.FragmentStockListBinding
import com.srm.srmapp.databinding.RvItemStockBinding
import com.srm.srmapp.databinding.SearchDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class StockListFragment : Fragment() {
    private lateinit var binding: FragmentStockListBinding
    private lateinit var searchDialogBinding: SearchDialogBinding
    private lateinit var dialog: AlertDialog
    private val viewmodel by activityViewModels<StockViewmodel>()
    private lateinit var adapter: Adapter<RvItemStockBinding, Food>
    private lateinit var argTitle: String
    private lateinit var argFoodType: Food.FoodType
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStockListBinding.inflate(inflater, container, false)
        searchDialogBinding = SearchDialogBinding.inflate(requireActivity().layoutInflater)
        argTitle = arguments?.getString("title") ?: "ERROR"
        argFoodType = Food.parseId(arguments?.getInt("id") ?: -1)
        setupView(argTitle, argFoodType)
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        viewmodel.getFoodListLiveData().observe(viewLifecycleOwner) {
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
        if (viewmodel.getFoodListLiveData().value == null)
            viewmodel.refreshFoodList()
    }

    @SuppressLint("InflateParams")
    private fun setupView(title: String, id: Food.FoodType) {
        binding.tvStockTitle.text = title

        // setup adapter
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

        // setup RV
        binding.rvItems.adapter = adapter
        binding.rvItems.layoutManager = LinearLayoutManager(activity)
        binding.btAdd.setOnClickListener {
            findNavController().navigate(R.id.action_stockListFragment_to_stockAddFragment, Bundle().let {
                it.putSerializable("type", id)
                it
            })
        }
        // setup diaglog
        dialog = AlertDialog.Builder(requireContext()).let {
            it.setView(searchDialogBinding.root)
            it.setPositiveButton("Buscar") { _, _ ->
                val query = searchDialogBinding.edFind.text.toString()
                val idx = adapter.getList().indexOfFirst { food ->
                    searchDialogBinding.edFind.setText("")
                    food.name == query
                }
                Timber.d("idx $idx")
                if (idx != -1) {
                    Snackbar.make(binding.root, adapter.getList()[idx].toString(), Snackbar.LENGTH_SHORT)
                        .show()
                    binding.rvItems.scrollToPosition(idx)
                } else {
                    Snackbar.make(binding.root, "Not found", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
            it.create()
        }

        binding.btnFind.setOnClickListener {
            dialog.show()
        }

        binding.srFoodRefresh.setOnRefreshListener {
            viewmodel.refreshFoodList(/* TODO ADD Type */)
        }
        binding.srFoodRefresh.isEnabled = true
        binding.btback.setOnClickListener {
            findNavController().navigate(R.id.action_stockListFragment_to_stockMainFragment)
        }
    }
}