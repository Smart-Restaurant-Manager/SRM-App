package com.srm.srmapp.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.ui.common.SrmButton
import com.srm.srmapp.ui.common.SrmHeader
import com.srm.srmapp.ui.destinations.RecipeScreenDestination
import com.srm.srmapp.ui.theme.ButtonColor1
import com.srm.srmapp.ui.theme.padding


@Destination
@Composable
fun MenuScreen(navigator: DestinationsNavigator) {
    val buttonNames = listOf(
        Pair(R.string.entrantes) { navigator.navigate(RecipeScreenDestination(Recipe.RecipeType.ENTRANTE)) },
        Pair(R.string.first_plate) { navigator.navigate(RecipeScreenDestination(Recipe.RecipeType.FIRST_PLATE)) },
        Pair(R.string.second_plate) { navigator.navigate(RecipeScreenDestination(Recipe.RecipeType.SECOND_PLATE)) },
        Pair(R.string.postres) { navigator.navigate(RecipeScreenDestination(Recipe.RecipeType.DESERT)) },
        Pair(R.string.bebidas) { navigator.navigate(RecipeScreenDestination(Recipe.RecipeType.DRINK)) },
        Pair(R.string.complementos) { navigator.navigate(RecipeScreenDestination(Recipe.RecipeType.COMPLEMENTS)) })

    SrmHeader(title = stringResource(R.string.Carta)) { navigator.navigateUp() }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(8.dp)) {
            items(buttonNames) { (id, onclick) ->
                SrmButton(modifier = Modifier
                    .padding(padding)
                    .width(150.dp)
                    .height(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ButtonColor1),
                    onClick = onclick,
                    text = stringResource(id = id))
            }
        }
    }
}
