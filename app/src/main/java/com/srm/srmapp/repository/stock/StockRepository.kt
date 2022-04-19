package com.srm.srmapp.repository.stock

import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Stock
import com.srm.srmapp.repository.BaseRepository
import javax.inject.Inject

class StockRepository @Inject constructor(private val api: StockInterface) : BaseRepository() {
    suspend fun getFood() = safeApiCall({
        api.getFood()
    }) {
        it.toFoodList()
    }

    suspend fun getFood(id: Int) = safeApiCall({
        api.getFood(id)
    }) {
        it.toFood()
    }

    suspend fun getStock() = safeApiCall({
        api.getStock()
    }) {
        it.toStockList()
    }

    suspend fun getStock(id: Int) = safeApiCall({
        api.getStock(id)
    }) {
        it.toStock()
    }

    suspend fun getFoodStock(id: Int) = safeApiCall({
        api.getFoodStock(id)
    }) {
        it.toStockList()
    }

    suspend fun postFood(food: Food) = safeApiCall({
        api.postFood(food.toJsonObject())
    }) {
        "New food added"
    }

    suspend fun putFood(food: Food) = safeApiCall({
        api.putFood(food.foodId, food.toJsonObject())
    }) {
        "Food modified"
    }

    suspend fun deleteFood(food: Food) = safeApiCall({
        api.deleteFood(food.foodId)
    }) {
        "Food deleted"
    }

    suspend fun postStock(stock: Stock) = safeApiCall({
        api.postStock(stock.toJsonObject())
    }) {
        "Stock added"
    }

    suspend fun putStock(stock: Stock) = safeApiCall({
        api.putStock(stock.stockId, stock.toJsonObject())
    }) {
        "Stock modified"
    }

    suspend fun deleteStock() = safeApiCall({
        api.deleteStock(1)
    }) {
        "Stock deleted"
    }
}