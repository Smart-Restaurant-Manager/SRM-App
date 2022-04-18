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
    fun getFoodListLiveData() = _foodList as LiveData<Resource<List<Food>>>

    private val _food: MutableLiveData<Resource<Food>> = MutableLiveData()
    fun getFoodLiveData() = _food as LiveData<Resource<Food>>

    private val _stockLisst: MutableLiveData<Resource<List<Stock>>> = MutableLiveData()
    fun getStockListLiveData() = _stockLisst as LiveData<Resource<List<Stock>>>

    private val _stock: MutableLiveData<Resource<Stock>> = MutableLiveData()
    fun getStockLiveData() = _stock as LiveData<Resource<Stock>>


    fun refreshStockList(/*TODO filter list by type, add get range */) {
        _stockLisst.value = Resource.Loading()
        viewModelScope.launchException {
            val stock = stockRepository.getStock()
            _stockLisst.postValue(stock)
        }
    }

    fun refreshFoodList(/*TODO filter list by type, add get range*/) {
        _foodList.value = Resource.Loading()
        viewModelScope.launchException {
            val food = stockRepository.getFood()
            _foodList.postValue(food)
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

    fun addFood(food: Food) {
        viewModelScope.launchException {
           stockRepository.postFood(food)
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
            list.find { it.id == id }?.let {
                _food.value = Resource.Success(data = it)
            }
        }
    }

    fun getStockbyId(id: Int) {
        val res = _stockLisst.value
        val list = res?.data
        if (list != null) {
            list.find { it.stockId == id }?.let {
                _stock.value = Resource.Success(data = it)
            }
        }
    }
}