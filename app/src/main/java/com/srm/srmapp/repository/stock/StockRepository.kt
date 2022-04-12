package com.srm.srmapp.repository.stock

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

    suspend fun postStock() = safeApiCall({
        api.postFood()
    }){

    }
}