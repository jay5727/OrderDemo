package com.jay.orderdemo.remote

import androidx.lifecycle.LiveData
import com.jay.orderdemo.api.ApiResponse
import com.jay.orderdemo.model.OrdersResponseModel
import retrofit2.http.GET

interface OrderService {

    /**
     * Get the list of Orders wrapped in [OrdersResponseModel] object.
     *
     *
     * @return [OrdersResponseModel] response wrapped in LiveData<ApiResponse<T>>
     */
    @GET("clients")
    fun getOrdersList(): LiveData<ApiResponse<OrdersResponseModel>>
}
