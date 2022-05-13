package com.srm.srmapp.data.models

import com.srm.srmapp.R
import com.srm.srmapp.data.dto.orders.body.OrderObject
import com.srm.srmapp.repository.orders.OrdersInterface
import java.time.LocalDateTime

data class Order(
    val orderId: Int = -1,
    val bookingId: Int = -1,
    var status: Status = Status.None(),
    val booking: Booking? = null,
    val recipe: List<Recipe>? = null,
    val recipeList: List<OrderRecipe>,
) {
    data class OrderRecipe(val recipeId: Int, val quantity: Int, val price: Double, val type: Int)

    sealed class Status(val data: String) {
        override fun toString(): String {
            return data
        }

        fun getStringId() = when (this) {
            is Cancelled -> R.string.cancelled
            is Confirmed -> R.string.confirmed
            is Delievered -> R.string.delievered
            is InProcess -> R.string.inprocess
            is Paid -> R.string.paid
            is Waiting -> R.string.waiting
            is None -> R.string.all
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
        OrderObject(bookingId = bookingId, recipes = recipeList.map { OrderObject.Recipe(it.recipeId, it.quantity, it.price, it.type) })
}