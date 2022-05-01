package com.srm.srmapp.data.models

import com.srm.srmapp.data.dto.orders.body.OrderObject

data class Order(val orderId: Int = -1, val bookingId: Int = -1, val status: String, val recipeList: List<OrderRecipe>) {
    data class OrderRecipe(val recipeId: Int, val quantity: Int, val price: Double, val type: Int)

    fun toJsonObject() =
        OrderObject(bookingId = bookingId, recipes = recipeList.map { OrderObject.Recipe(it.recipeId, it.quantity, it.price, it.type) })
}