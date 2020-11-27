package com.jay.orderdemo.model

/**
 * Wrapper Response of Order Model
 */
data class OrdersResponseModel(
    val orders: List<Order>? = null
)