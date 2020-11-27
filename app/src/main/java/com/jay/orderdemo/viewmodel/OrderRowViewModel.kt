package com.jay.orderdemo.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.jay.orderdemo.base.BaseViewModel
import com.jay.orderdemo.model.Order

/** 
 * View model for row item of Order List inherits [ViewModel] 
 */
class OrderRowViewModel : BaseViewModel() {
    val item = ObservableField<Order>()
}