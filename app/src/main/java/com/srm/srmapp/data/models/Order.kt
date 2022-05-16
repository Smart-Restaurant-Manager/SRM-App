package com.srm.srmapp.data.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.srm.srmapp.R
import com.srm.srmapp.data.dto.orders.body.OrderObject
import com.srm.srmapp.repository.orders.OrdersInterface

data class Order(
    val orderId: Int = -1,
    val bookingId: Int = -1,
    val status: Status = Status.None(),
    val booking: Booking? = null,
    val recipeList: List<OrderRecipe>,
) : GetId {
    data class OrderRecipe(val recipeId: Int, val name: String = "", val quantity: Int, val price: Double = 0.0, val type: Int = 0) : GetId {
        override fun getId(): Int {
            return recipeId
        }
    }

    sealed class Status(val data: String) {
        override fun toString(): String {
            return data
        }

        @Composable
        fun getString(): String = when (this) {
            is Cancelled -> stringResource(R.string.cancelled)
            is Confirmed -> stringResource(R.string.confirmed)
            is Delievered -> stringResource(R.string.delievered)
            is InProcess -> stringResource(R.string.inprocess)
            is Paid -> stringResource(R.string.paid)
            is Waiting -> stringResource(R.string.waiting)
            is None -> stringResource(R.string.all)
        }

        fun getId() = when (this) {
            is Waiting -> 1
            is Confirmed -> 2
            is Cancelled -> 3
            is InProcess -> 4
            is Delievered -> 5
            is Paid -> 6
            is None -> 1
        }

        companion object {
            val STAUS_UI =
                listOf(None(), Waiting(), Confirmed(), InProcess(),
                    Cancelled(), Delievered(), Paid())
        }

        class None : Status("")
        class Waiting : Status(OrdersInterface.WAITING)
        class Confirmed : Status(OrdersInterface.CONFIRMED)
        class InProcess : Status(OrdersInterface.IN_PROCESS)
        class Cancelled : Status(OrdersInterface.WAITING)
        class Delievered : Status(OrdersInterface.DELIEVERED)
        class Paid : Status(OrdersInterface.PAID)
    }

    fun toJsonObject() =
        OrderObject(orderStatus = status.getId(),
            bookingId = bookingId,
            recipes = recipeList.map { OrderObject.Recipe(it.recipeId, it.quantity, it.price) })


    override fun getId(): Int {
        return orderId
    }
}