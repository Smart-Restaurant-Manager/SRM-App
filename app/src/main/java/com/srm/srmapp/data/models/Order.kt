package com.srm.srmapp.data.models

import com.srm.srmapp.data.dto.orders.body.OrderObject
import com.srm.srmapp.repository.orders.OrdersInterface
import timber.log.Timber

data class Order(val orderId: Int = -1, val bookingId: Int = -1, val status: Status = Status.None(), val recipeList: List<OrderRecipe>) {
    data class OrderRecipe(val recipeId: Int, val quantity: Int, val price: Double, val type: Int)

    sealed class Status(val data: String) {
        override fun toString(): String {
            return data
        }

        class None : Status("")
        class Waiting : Status(OrdersInterface.WAITING)
        class Confirmed : Status(OrdersInterface.CONFIRMED)
        class InProcess : Status(OrdersInterface.IN_PROCESS)
        class Cancelled : Status(OrdersInterface.WAITING)
        class Delievered : Status(OrdersInterface.DELIEVERED)
        class Paid : Status(OrdersInterface.PAID)


    }

    companion object {
        fun parseStatus(s: String): Status = when (s) {
            OrdersInterface.WAITING -> Status.Waiting()
            OrdersInterface.CONFIRMED -> Status.Confirmed()
            OrdersInterface.IN_PROCESS -> Status.InProcess()
            OrdersInterface.CANCELLED -> Status.Cancelled()
            OrdersInterface.PAID -> Status.Paid()
            OrdersInterface.DELIEVERED -> Status.Delievered()
            else -> {
                Timber.e("Order status not found!")
                Status.None()
            }
        }
    }

    fun toJsonObject() =
        OrderObject(bookingId = bookingId, recipes = recipeList.map { OrderObject.Recipe(it.recipeId, it.quantity, it.price, it.type) })
}