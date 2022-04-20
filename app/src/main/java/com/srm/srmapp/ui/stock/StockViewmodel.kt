package com.srm.srmapp.ui.stock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srm.srmapp.Resource
import com.srm.srmapp.Utils.launchException
import com.srm.srmapp.data.models.Food
import com.srm.srmapp.data.models.Stock
import com.srm.srmapp.repository.stock.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StockViewmodel @Inject constructor(private val stockRepository: StockRepository) : ViewModel() {
    init {
        Timber.d("INIT")
    }

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

    private val _status: MutableLiveData<Resource<String>> = MutableLiveData()
    val status: LiveData<Resource<String>>
        get() = _status

    fun clearStcokList() {
        _stockList.value = Resource.Empty()
    }

    fun clearFoodList() {
        _foodList.value = Resource.Empty()
    }

    fun clearStatus() {
        Timber.d("Clear Status ${_status.value}")
        _status.value = Resource.Empty()
    }

    fun refreshStockList(/*TODO filter list by type, add get range */) {
        _stockList.value = Resource.Loading()
        viewModelScope.launchException {
            val stock = stockRepository.getStock()
            _stockList.postValue(stock)
        }
    }

    fun refreshFoodList(/*TODO filter list by type, add get range*/) {
        _foodList.value = Resource.Loading()
        viewModelScope.launchException {
            val food = stockRepository.getFood()
            _foodList.postValue(food)
        }
    }

    fun deleteFood(food: Food) {
        viewModelScope.launchException {
            val msg = stockRepository.deleteFood(food)
            Timber.d("Delete food $msg")
            _status.postValue(msg)
        }
    }

    fun getFoodStock(food: Food) {
        viewModelScope.launchException {
            val stockList = stockRepository.getFoodStock(food)
            _stockList.postValue(stockList)
        }
    }

    fun getFoodBy(id: Int) {
        viewModelScope.launchException {
            val food = stockRepository.getFood(id)
            _food.postValue(food)
        }
    }

    fun getStockBy(id: Int) {
        viewModelScope.launchException {
            val stock = stockRepository.getStock(id)
            _stock.postValue(stock)
        }
    }

    fun addFood(type: String, name: String, units: String) {
        val food = Food(type=type, name = name, units = units)
        viewModelScope.launchException {
            _status.postValue(stockRepository.postFood(food))
        }
    }

    fun addStock(stock: Stock) {
        viewModelScope.launchException {
            // TODO
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