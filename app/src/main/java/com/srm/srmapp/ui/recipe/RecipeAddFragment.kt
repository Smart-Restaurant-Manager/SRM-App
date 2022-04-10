package com.srm.srmapp.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.databinding.FragmentMenuItemAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeAddFragment : Fragment() {
    private lateinit var binding: FragmentMenuItemAddBinding
    private val viewmodel by viewModels<RecipeViewmodel>()
    private lateinit var argRecipeType: Recipe.RecipeType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMenuItemAddBinding.inflate(inflater, container, false)
        argRecipeType = arguments?.getSerializable("type") as Recipe.RecipeType
        setupView()
        return binding.root
    }
    private fun setupView() {
        binding.btCartaAdd.setOnClickListener {
            binding.apply {
                val name = etCartaName.text.toString().ifEmpty {
                    Snackbar.make(it, "Invalid Name", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                } //El price es un Float no se si lo he hecho bien aqui, o lo tengo que pasar a String
                val price = etCartaPrice.text.toString().ifEmpty {
                    Snackbar.make(it, "Invalid Price", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }.toFloat()
                val ing1 = eting1.toString().ifEmpty {
                    Snackbar.make(it, "Invalid Ing1", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                val ing2 = eting2.toString().ifEmpty {
                    Snackbar.make(it, "Invalid Ing2", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                val ing3 = eting1.toString().ifEmpty {
                    Snackbar.make(it, "Invalid Ing3", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                val ing4 = eting4.toString().ifEmpty {
                    Snackbar.make(it, "Invalid Ing4", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                //Añadir la receta al addRecipe
               // val recipe = Recipe(argRecipeType,-1,name,price, listOf(Food(Food.FoodType.CARNE,-1,"hey",))
               // viewmodel.addRecipe(recipe)
                findNavController().popBackStack()
            }
        }
        binding.btCartaDelete.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}