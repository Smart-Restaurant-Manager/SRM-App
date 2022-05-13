package com.srm.srmapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.ui.bookings.BookingViewModel
import com.srm.srmapp.ui.menu.RecipeViewmodel
import com.srm.srmapp.ui.order.OrderViewModel
import com.srm.srmapp.ui.stock.StockViewmodel
import com.srm.srmapp.ui.theme.SMRappTheme
import dagger.hilt.android.AndroidEntryPoint
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
                    dependency(hiltViewModel<StockViewmodel>(this@MainActivity))
                    dependency(hiltViewModel<OrderViewModel>(this@MainActivity))
                    dependency(hiltViewModel<BookingViewModel>(this@MainActivity))
                    dependency(hiltViewModel<RecipeViewmodel>(this@MainActivity))
                })
            }
        }
    }
}