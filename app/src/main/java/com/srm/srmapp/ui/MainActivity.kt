package com.srm.srmapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.ui.menu.RecipeViewmodel
import com.srm.srmapp.ui.stock.StockViewmodel
import com.srm.srmapp.ui.theme.SMRappTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userSession: UserSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SMRappTheme(false) {
                DestinationsNavHost(navGraph = NavGraphs.root, dependenciesContainerBuilder = {
                    dependency(userSession)
                    Timber.d("dependency ${destination.route}")
                    Timber.d("dependency ${NavGraphs.root.destinations}")
                    if (NavGraphs.root.destinations.contains(destination)) {
                        val parentEntry = remember {
                            navController.getBackStackEntry(NavGraphs.root.route)
                        }
                        dependency(hiltViewModel<StockViewmodel>(parentEntry))
                        dependency(hiltViewModel<RecipeViewmodel>(parentEntry))
                    }
                })
            }
        }
    }
}