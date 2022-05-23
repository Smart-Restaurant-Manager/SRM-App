package com.srm.srmapp.ui.stock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.srm.srmapp.Resource
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Stock
import com.srm.srmapp.repository.stock.StockRepository
import com.srm.srmapp.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StockViewmodel @Inject constructor(private val stockRepository: StockRepository, userSession: UserSession) : BaseViewModel(userSession) {
    private val _foodList: MutableLiveData<Resource<List<Food>>> = MutableLiveData()
    val foodList: LiveData<Resource<List<Food>>>
        get() = _foodList

    private val _food: MutableLiveData<Resource<Food>> = MutableLiveData()
    val food: LiveData<Resource<Food>>
        get() = _food

    private val _stockList: MutableLiveData<Resource<List<Stock>>> = MutableLiveData()
    val stockList: LiveData<Resource<List<Stock>>>
        get() = _stockList

    private val _stock: MutableLiveData<Resource<Stock>> = MutableLiveData()
    val stock: LiveData<Resource<Stock>>
        get() = _stock

    init {
        Timber.d("INIT")
    }

    fun clearStcokList() {
        _stockList.value = Resource.Empty()
    }

    fun clearFoodList() {
        _foodList.value = Resource.Empty()
    }

    fun refreshStockList() {
        fetchResource(_stockList) {
            stockRepository.getStock()
        }
    }

    fun refreshFoodList() {
        fetchResource(_foodList) {
            stockRepository.getFood()
        }
    }

    fun deleteFood(food: Food) {
        fetchResource(_status, onSuccess = {
            _foodList.value?.data?.toMutableList()?.let { list ->
                list.remove(food)
                _foodList.postValue(Resource.Success(list.toList()))
            }
        }) {
            val msg = stockRepository.deleteFood(food)
            Timber.d("Delete food $msg")
            msg
        }
    }

    fun deleteStock(stock: Stock) {
        fetchResource(_status, onSuccess = {
            _stockList.value?.data?.toMutableList()?.let { list ->
                list.remove(stock)
                _stockList.postValue(Resource.Success(list.toList()))
            }
        }, repositoryCall = {
            val msg = stockRepository.deleteStock(stock)
            Timber.d("Delete stock $msg")
            msg
        })
    }


    fun getFoodStock(food: Food) {
        fetchResource(_stockList) {
            stockRepository.getFoodStock(food)
        }
    }

    fun getFoodBy(id: Int) {
        fetchResource(_food) {
            stockRepository.getFood(id)
        }
    }

    fun getStockBy(id: Int) {
        fetchResource(_stock) {
            stockRepository.getStock(id)
        }
    }

    fun addFood(food: Food) {
        fetchResource(_status, onSuccess = {
            refreshFoodList()
        }) {
            stockRepository.postFood(food)
        }
    }

    fun addStock(food: Food, quantity: Float, expirationDate: LocalDate) {
        val stock = Stock(foodId = food.foodId, quantity = quantity, stockId = -1, expirationDate = expirationDate)
        fetchResource(_status, onSuccess = {
            refreshStockList()
        }) {
            stockRepository.postStock(stock)
        }
    }


    fun getFoodById(id: Int) {
        val res = _foodList.value
        val list = res?.data
        if (list != null) {
            list.find { it.foodId == id }?.let {
                _food.value = Resource.Success(data = it)
            }
        }
    }

    fun putStock(stock: Stock) {
        fetchResource(_status, onSuccess = {
            refreshStockList()
        }) {
            stockRepository.putStock(stock.stockId, stock)
        }
    }

    fun putFood(food: Food) {
        fetchResource(_status, onSuccess = {
            refreshFoodList()
        }) {
            stockRepository.putFood(food)
        }
    }

    fun getStockbyId(id: Int) {
        val res = _stockList.value
        val list = res?.data
        if (list != null) {
            list.find { it.stockId == id }?.let {
                _stock.value = Resource.Success(data = it)
            }
        }
    }
}