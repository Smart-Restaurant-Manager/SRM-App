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
import com.srm.srmapp.databinding.FragmentMenuItemAddOrModBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeModFragment : Fragment() {
    private lateinit var binding: FragmentMenuItemAddOrModBinding
    private val viewmodel by viewModels<RecipeViewmodel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMenuItemAddOrModBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.apply {
            btCartaDelete.visibility = View.GONE
            btCartaAddOrMod.apply {
                text = getString(R.string.modificar)
                setOnClickListener {
                    binding.apply {
                        val name = etCartaName.text.toString().ifEmpty {
                            Snackbar.make(it, "Invalid Name", Snackbar.LENGTH_LONG).show()
                            return@setOnClickListener
                        }//El price es un Float no se si lo he hecho bien aqui, o lo tengo que pasar a String
                        val price = etCartaPrice.text.toString().ifEmpty {
                            Snackbar.make(it, "Invalid Price", Snackbar.LENGTH_LONG).show()
                            return@setOnClickListener
                        }.toFloat()
                        //Añadir la receta al modRecipe
                        // val recipe = Recipe(argRecipeType,-1,name,price, listOf(Food(Food.FoodType.CARNE,-1,"hey",))
                        // viewmodel.modRecipe(recipe)
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    companion object ARG {
        const val RECIPE = "recipe"
    }
}