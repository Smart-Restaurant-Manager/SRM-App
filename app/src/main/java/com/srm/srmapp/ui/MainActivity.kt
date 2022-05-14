package com.srm.srmapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.ui.bookings.BookingViewModel
import com.srm.srmapp.ui.common.BaseViewModel
import com.srm.srmapp.ui.common.SrmText
import com.srm.srmapp.ui.common.SrmTextButton
import com.srm.srmapp.ui.login.LoginViewModel
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


                // TODO Queue messages from viewmodels

                for (viewmodel in arrayListOf<BaseViewModel>(hiltViewModel<StockViewmodel>(this@MainActivity),
                    hiltViewModel<LoginViewModel>(this@MainActivity),
                    hiltViewModel<OrderViewModel>(this@MainActivity),
                    hiltViewModel<RecipeViewmodel>(this@MainActivity),
                    hiltViewModel<BookingViewModel>(this@MainActivity))) {
                    val s = viewmodel.status

                }

                var openDialog by remember { mutableStateOf(true /* if queue is not empty*/) }
                if (openDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            // TODO Clear last
                            openDialog = false
                        },
                        confirmButton = {
                            SrmTextButton(onClick = {
                                // TODO Clear last
                                openDialog = false
                            }, text = "Confirmar")
                        },
                        text = { SrmText(text = "TODO GET ALERT") }
                    )
                }
                DestinationsNavHost(navGraph = NavGraphs.root, dependenciesContainerBuilder = {
                    dependency(userSession)
                    dependency(hiltViewModel<StockViewmodel>(this@MainActivity))
                    dependency(hiltViewModel<LoginViewModel>(this@MainActivity))
                    dependency(hiltViewModel<OrderViewModel>(this@MainActivity))
                    dependency(hiltViewModel<BookingViewModel>(this@MainActivity))
                    dependency(hiltViewModel<RecipeViewmodel>(this@MainActivity))
                })
            }
        }
    }
}