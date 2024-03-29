package com.srm.srmapp.data.dto.orders.response


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.srm.srmapp.data.models.Booking
import com.srm.srmapp.data.models.Order
import timber.log.Timber
import java.time.LocalDateTime

@Keep
data class OrderResponse(
    @SerializedName("data")
    val `data`: Data,
) {
    data class Data(
        @SerializedName("type")
        val type: String, // orders
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("attributes")
        val attributes: Attributes,
    ) {
        data class Attributes(
            @SerializedName("order_status_id")
            val orderStatusId: Int, // 11
            @SerializedName("booking_id")
            val bookingId: Int, // 22
            @SerializedName("status")
            val status: Status,
            @SerializedName("booking")
            val booking: Booking,
            @SerializedName("recipes")
            val recipes: List<Recipe>,
            @SerializedName("created_at")
            val createdAt: LocalDateTime, // 2012/03/06 17:33:07
            @SerializedName("updated_at")
            val updatedAt: LocalDateTime, // 2012/03/06 17:33:07
        ) {
            data class Status(
                @SerializedName("type")
                val type: String, // statuses
                @SerializedName("id")
                val id: Int, // 1
                @SerializedName("attributes")
                val attributes: Attributes,
            ) {
                data class Attributes(
                    @SerializedName("status")
                    val status: String, // Completada
                    @SerializedName("created_at")
                    val createdAt: LocalDateTime, // 2012/03/06 17:33:07
                    @SerializedName("updated_at")
                    val updatedAt: LocalDateTime, // 2012/03/06 17:33:07
                )
            }

            data class Booking(
                @SerializedName("type")
                val type: String, // bookings
                @SerializedName("id")
                val id: Int, // 1
                @SerializedName("attributes")
                val attributes: Attributes,
            ) {
                data class Attributes(
                    @SerializedName("name")
                    val name: String, // foo booking
                    @SerializedName("email")
                    val email: String, // foo@foo.com
                    @SerializedName("phone")
                    val phone: String, // +34 111 111 111
                    @SerializedName("date")
                    val date: LocalDateTime, // 2022/04/13 21:30:00
                    @SerializedName("people")
                    val people: Int, // 6
                    @SerializedName("table")
                    val table: String, // foo
                )
            }

            data class Recipe(
                @SerializedName("order_id")
                val orderId: Int, // 1
                @SerializedName("recipe_name")
                val name: String, // Patata
                @SerializedName("recipe_id")
                val recipeId: Int, // 1
                @SerializedName("quantity")
                val quantity: Float, // 3
                @SerializedName("price")
                val price: Float, // 30.55
                @SerializedName("type")
                val type: Int, // 1
                @SerializedName("created_at")
                val createdAt: LocalDateTime, // 2012/03/06 17:33:07
                @SerializedName("updated_at")
                val updatedAt: LocalDateTime, // 2012/03/06 17:33:07
            )
        }
    }
}

fun OrderResponse.toOrder() = data.toOrder()

fun parseStatus(s: String): Order.Status = when (s) {
    "En espera" -> Order.Status.Waiting()
    "Confirmada" -> Order.Status.Confirmed()
    "En procés" -> Order.Status.InProcess()
    "Cancel·lada" -> Order.Status.Cancelled()
    "Pagada" -> Order.Status.Paid()
    "Servida" -> Order.Status.Delievered()
    else -> {
        Timber.e("Order status not found!")
        Order.Status.None()
    }
}

fun OrderResponse.Data.getBooking(): Booking {
    val id = attributes.booking.id
    val attributes = attributes.booking.attributes
    return Booking(name = attributes.name,
        bookingId = id,
        email = attributes.email,
        phone = attributes.phone,
        date = attributes.date,
        people = attributes.people,
        table = attributes.table)
}

fun OrderResponse.Data.toOrder() =
    Order(
        orderId = id,
        bookingId = attributes.bookingId,
        status = parseStatus(attributes.status.attributes.status),
        booking = getBooking(),
        recipeList = attributes.recipes.map {
            Order.OrderRecipe(recipeId = it.recipeId,
                quantity = it.quantity,
                price = it.price,
                type = it.type,
                name = it.name)
        })