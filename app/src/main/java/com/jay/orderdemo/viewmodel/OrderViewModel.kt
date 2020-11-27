package com.jay.orderdemo.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.google.android.gms.maps.model.LatLng
import com.jay.orderdemo.api.AbsentLiveData
import com.jay.orderdemo.api.Resource
import com.jay.orderdemo.base.BaseViewModel
import com.jay.orderdemo.model.OrdersResponseModel
import com.jay.orderdemo.repository.OrderRepository

/**
 * OrderViewModel - view model for order list inherits [BaseViewModel]
 */
class OrderViewModel @ViewModelInject constructor(
    private val repository: OrderRepository
) : BaseViewModel() {

    val TAG = OrderViewModel::class.java.simpleName
    val shouldShowLoader = ObservableBoolean(true)

    private val orderPageLiveData: MutableLiveData<Int> = MutableLiveData()
    val latLongLiveData: MutableLiveData<LatLng> = MutableLiveData()
    val orderListLiveData: LiveData<Resource<OrdersResponseModel>>

    init {
        this.orderListLiveData = orderPageLiveData.switchMap {
            orderPageLiveData.value?.let {
                //intentionally not passing page number here, since no pagination...
                repository.getOrdersList()
            } ?: AbsentLiveData.create()
        }
    }

    fun postOrderPage(id: Int?) = orderPageLiveData.postValue(id)

}