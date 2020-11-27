package com.jay.orderdemo.repository

import androidx.lifecycle.LiveData
import com.jay.orderdemo.api.ApiResponse
import com.jay.orderdemo.api.NetworkResource
import com.jay.orderdemo.api.Resource
import com.jay.orderdemo.model.OrdersResponseModel
import com.jay.orderdemo.remote.OrderService
import javax.inject.Inject

/**
 * @param orderService order service api implementation
 */
class OrderRepository
@Inject constructor(
    private val orderService: OrderService
) {
    fun getOrdersList(): LiveData<Resource<OrdersResponseModel>> {
        return object : NetworkResource<OrdersResponseModel>() {
            override fun fetchService(): LiveData<ApiResponse<OrdersResponseModel>> {
                return orderService.getOrdersList()
            }
        }.asLiveData()
    }
}