package com.srm.srmapp.ui.orders

import com.srm.srmapp.repository.orders.OrdersRepository
import com.srm.srmapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(val ordersRepository: OrdersRepository) : BaseViewModel() {
}