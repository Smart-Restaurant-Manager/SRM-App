package com.srm.srmapp.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.databinding.FragmentMenuCardListBinding
import com.srm.srmapp.databinding.RvItemRecipeBinding
import com.srm.srmapp.ui.common.Adapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MenuCardListFragment : Fragment() {
    private lateinit var binding: FragmentMenuCardListBinding
    private lateinit var adapter: Adapter<RvItemRecipeBinding, Recipe>
    private val viewmodel by activityViewModels<RecipeViewmodel>()
    private var id: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Timber.d("Create")
        binding = FragmentMenuCardListBinding.inflate(inflater, container, false)
        id = arguments?.getInt("id")
        setupView()
        setupObservables()
        return binding.root
    }

    private fun setupObservables() {
        if (viewmodel.getRecipeListLiveData().value == null || viewmodel.getRecipeListLiveData().value?.data !is Resource.Success<*>)
            viewmodel.refreshRecipeList()
        viewmodel.getRecipeListLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    binding.srRecipeRefresh.isRefreshing = false
                }
                is Resource.Loading -> {
                    binding.srRecipeRefresh.isRefreshing = true
                }
                is Resource.Success -> {
                    binding.srRecipeRefresh.isRefreshing = false
                    val data = it.data
                    if (data != null) {
                        adapter.updateItems(data)
                        Timber.d("Got ${data.size} recipes for the id $id ${adapter.itemCount}")
                    }
                }
                else -> {}
            }
        }
    }

    private fun setupView() {
        adapter = Adapter(listOf(), R.layout.rv_item_recipe, { // binding
            RvItemRecipeBinding.bind(it)
        }, { view, recipe -> // setup item view
            view.apply {
                Timber.d(recipe.name)
                ivImage.setImageResource(R.drawable.lis_imagen_1) // for now we use a static image
                tvRecipeName.text = recipe.name
                btMod.setOnClickListener {
                    findNavController().navigate(R.id.action_menuCardList_to_recipeModFragment, Bundle().apply {
                        putParcelable(RecipeModFragment.RECIPE, recipe)
                    })
                }
            }
        }) { view, recipe -> // on Click Listener

        }

        binding.rvRecipeList.apply {
            adapter = this@MenuCardListFragment.adapter
            binding.rvRecipeList.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)

        }
        binding.srRecipeRefresh.setOnRefreshListener {
            viewmodel.refreshRecipeList()
        }
        binding.btAdd.setOnClickListener {
            id?.let {
                findNavController().navigate(R.id.action_menuCardList_to_recipeAddFragment, Bundle().apply {
                    putSerializable("type", Recipe.parseId(it))
                })
            }
        }
    }
}