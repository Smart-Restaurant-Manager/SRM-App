package com.srm.srmapp.repository.stock

import com.srm.srmapp.Resource
import com.srm.srmapp.data.dto.stock.response.toFood
import com.srm.srmapp.data.dto.stock.response.toFoodList
import com.srm.srmapp.data.dto.stock.response.toStock
import com.srm.srmapp.data.dto.stock.response.toStockList
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Stock
import com.srm.srmapp.repository.BaseRepository
import timber.log.Timber
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

    suspend fun getFoodStock(food: Food) = safeApiCall({
        api.getFoodStock(food.foodId)
    }) {
        it.toStockList()
    }

    suspend fun postFood(food: Food) = safeApiCall({
        api.postFood(food.toJsonObject())
    }) {
        "Nuevo alimento añadido"
    }

    suspend fun putFood(food: Food) = safeApiCall({
        api.putFood(food.foodId, food.toJsonObject())
    }) {
        "Alimento modificado"
    }

    suspend fun deleteFood(food: Food): Resource<String> {
        val res = safeApiCall({
            api.deleteFood(food.foodId)
        }) {
            "Alimento eliminado"
        }
        Timber.d(res.toString())
        return res
    }

    suspend fun postStock(stock: Stock) = safeApiCall({
        api.postStock(stock.toJsonObject())
    }) {
        "Stock añadido"
    }

    suspend fun putStock(id: Int, stock: Stock) = safeApiCall({
        api.putStock(id, stock.toJsonObject())
    }) {
        "Stock modificado"
    }

    suspend fun deleteStock(stock: Stock) = safeApiCall({
        api.deleteStock(stock.stockId)
    }) {
        "Stock eliminado"
    }
}