package com.jay.orderdemo.model

/**
 * @param name indicates name associated with order
 * @param location indicates [Location] of Order
 * @param phone indicates phone number associated with order
 * @param address indicates address of order
 */
data class Order(
    val name: String?,
    val location: Location?,
    val phone: Int?,
    val address: String?
)