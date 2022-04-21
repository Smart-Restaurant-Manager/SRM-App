package com.srm.srmapp.ui.menu

import android.text.Layout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.paging.compose.LazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.srm.srmapp.R
import com.srm.srmapp.ui.common.PreviewButtonTitleButton
import com.srm.srmapp.ui.common.SrmHeader
import com.srm.srmapp.ui.stock.StockViewmodel
import com.srm.srmapp.ui.theme.padding
import com.srm.srmapp.ui.common.SrmButton
import com.srm.srmapp.ui.destinations.EntrantesScreenDestination
import com.srm.srmapp.ui.recipe.EntrantesScreen
import com.srm.srmapp.ui.theme.ButtonColor1




@Destination
@Composable
fun MenuScreen(navigator: DestinationsNavigator,
               backStackEntry: NavBackStackEntry,
) {
    val buttonNames = listOf(
        Pair(R.string.entrantes){navigator.navigate(EntrantesScreenDestination()) },
        Pair(R.string.first_plate){},
        Pair(R.string.second_plate){},
        Pair(R.string.postres){},
        Pair(R.string.bebidas){},
        Pair(R.string.complementos){})

    SrmHeader(title = stringResource(R.string.Carta)) {navigator.navigateUp()}
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        LazyVerticalGrid(columns = GridCells.Fixed(2) , contentPadding = PaddingValues(8.dp)) {
           items(buttonNames){(id, onclick) ->
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
