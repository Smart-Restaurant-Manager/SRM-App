package com.srm.srmapp.data.models

import android.os.Parcelable
import com.srm.srmapp.R
import kotlinx.parcelize.Parcelize
import timber.log.Timber

@Parcelize
data class Recipe(val type:RecipeType, val id: Int, val name:String, val price: Float, val food: List<Food>? = null): Parcelable {
   enum class  RecipeType{
       NONE,ENTRANTE,FIRST_PLATE,
       SECOND_PLATE,DESERT,DRINK,
       COMPLEMENTS

   }

    companion object {
        fun parseId(id:Int) = when (id){
            R.id.btEntrantes -> RecipeType.ENTRANTE
            R.id.btPrimerPlato -> RecipeType.FIRST_PLATE
            R.id.btSegundoPlato -> RecipeType.SECOND_PLATE
            R.id.btPostres -> RecipeType.DESERT
            R.id.btBebidas -> RecipeType.DRINK
            R.id.btComplementos -> RecipeType.COMPLEMENTS

            else -> {
                Timber.w("Unkown id $id")
                Recipe.RecipeType.NONE
            }
        }
    }
    override fun toString(): String {
        return "$name  $id  $price "
    }

}