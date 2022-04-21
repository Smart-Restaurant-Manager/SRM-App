package com.srm.srmapp.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.models.Recipe
import com.srm.srmapp.ui.common.*
import com.srm.srmapp.ui.theme.paddingEnd
import com.srm.srmapp.ui.theme.paddingStart


@Destination
@Composable
fun EntrantesScreen(
    navigator: DestinationsNavigator,
    viewmodel: RecipeViewmodel = hiltViewModel(),
) {
    val RecipeList by viewmodel.getRecipeListLiveData().observeAsState(Resource.Empty())
    val Recipe by viewmodel.getRecipeLiveData().observeAsState(Resource.Empty())
    var popupState by remember { mutableStateOf(false) }
    var popupAddState by remember { mutableStateOf(false) }
    val refreshState = rememberSwipeRefreshState(RecipeList.isLoading())


    val selectedItems = rememberSaveable {
        mutableStateOf(setOf<Int>())
    }


    if (RecipeList.isEmpty()) viewmodel.refreshRecipeList()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = paddingStart, end = paddingEnd),
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        SrmAddTitleSearch(stringResource(id = R.string.entrantes),
            onClickSearch = {},
            onClickAdd = { popupAddState = true },
            onClickBack = { navigator.navigateUp() })
        SwipeRefresh(
            state = refreshState,
            onRefresh = { viewmodel.refreshRecipeList() }) {

            LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(8.dp))
            {

                if (RecipeList.isSuccess())
                    RecipeList.data?.let {
                        items(it) { recipe ->
                            RecipeItem(recipe = recipe) { popupState = !popupState }
                            if (popupState)
                                RecipeItemPopUp(recipe = recipe,
                                    onDismissRequest = { popupState = !popupState })

                        }
                    }
            }
        }
    }
    if (popupAddState) {
        var name by remember { mutableStateOf("") }
        var precio by remember { mutableStateOf("") }
        var ingredient1 by remember { mutableStateOf("") }
        var ingredient2 by remember { mutableStateOf("") }
        var ingredient3 by remember { mutableStateOf("") }
        var ingredient4 by remember { mutableStateOf("") }
        var ingredient5 by remember { mutableStateOf("") }


        SrmDialog(onDismissRequest = { popupAddState = false }) {
            SrmTextFieldHint(value = name, placeholder = stringResource(R.string.food_name), onValueChange = { name = it })
//            SrmTextFieldHint(value = precio, placeholder = stringResource(R.string.precio), onValueChange = { precio = it })
//            SrmTextFieldHint(value = ingredient1, placeholder = stringResource(R.string.ingredient1), onValueChange = { ingredient1 = it })
//            SrmTextFieldHint(value = ingredient2, placeholder = stringResource(R.string.ingredient2), onValueChange = { ingredient2 = it })
//            SrmTextFieldHint(value = ingredient3, placeholder = stringResource(R.string.ingredient3), onValueChange = { ingredient3 = it })
//            SrmTextFieldHint(value = ingredient4, placeholder = stringResource(R.string.ingredient4), onValueChange = { ingredient4 = it })
//            SrmTextFieldHint(value = ingredient5, placeholder = stringResource(R.string.ingredient5), onValueChange = { ingredient5 = it })

            TextButton(onClick = {
//                viewmodel.addRecipe(type, name, units)
            }) {
                SrmText(text = stringResource(R.string.add_food))
            }
        }
    }

}


@Composable
fun RecipeItem(recipe: Recipe, onclick: () -> Unit) {
    SrmText(text = recipe.name, textAlign = TextAlign.Center)
    SrmText(text = recipe.price.toString(), textAlign = TextAlign.Center)
}

@Composable
fun RecipeItemPopUp(
    recipe: Recipe,
    viewmodel: RecipeViewmodel = hiltViewModel(),
    onDismissRequest: () -> Unit = {},
) {
    SrmDialog(onDismissRequest = onDismissRequest) {
        SrmSelectableRow(
            item = recipe,
            horizontalArrangement = Arrangement.Start,
            onClick = {
                viewmodel.getRecipeBy(it.id)
                onDismissRequest.invoke()
            }) {


        }
    }
}


