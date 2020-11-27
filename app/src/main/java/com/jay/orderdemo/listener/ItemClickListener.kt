package com.jay.orderdemo.listener

import com.jay.orderdemo.model.Order

/**
 * An interface to track the clicks
 */
interface ItemClickListener {

    /**
     * Callback for Item clicked
     *
     * @param position item index
     * @param order clicked order item object
     */
    fun onItemClicked(position: Int, order: Order?)

    /**
     * Callback for Phone clicked
     *
     * @param order object containing info
     */
    fun onPhoneCallClicked(order: Order?)

    /**
     * Callback for Navigation clicked
     *
     * @param order object containing info
     */
    fun onNavigationClicked(order: Order?)

}