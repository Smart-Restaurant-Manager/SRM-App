package com.srm.srmapp.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.srm.srmapp.R
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.databinding.FragmentMenuItemAddOrModBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeAddFragment : Fragment() {
    private lateinit var binding: FragmentMenuItemAddOrModBinding
    private val viewmodel by viewModels<RecipeViewmodel>()
    private lateinit var argRecipeType: Recipe.RecipeType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMenuItemAddOrModBinding.inflate(inflater, container, false)
        argRecipeType = arguments?.getSerializable("type") as Recipe.RecipeType
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.apply {
            btCartaAddOrMod.apply {
                text = getString(R.string.a_adir_a_la_carta)
                setOnClickListener {
                    binding.apply {
                        val name = etCartaName.text.toString().ifEmpty {
                            Snackbar.make(it, "Invalid Name", Snackbar.LENGTH_LONG).show()
                            return@setOnClickListener
                        } //El price es un Float no se si lo he hecho bien aqui, o lo tengo que pasar a String
                        val price = etCartaPrice.text.toString().ifEmpty {
                            Snackbar.make(it, "Invalid Price", Snackbar.LENGTH_LONG).show()
                            return@setOnClickListener
                        }.toFloat()
                        //Añadir la receta al addRecipe
                        // val recipe = Recipe(argRecipeType,-1,name,price, listOf(Food(Food.FoodType.CARNE,-1,"hey",))
                        // viewmodel.addRecipe(recipe)
                        findNavController().popBackStack()
                    }
                }
            }
            btSelectFood.setOnClickListener {
                // TODO Dialog to select food
            }
        }
    }
}