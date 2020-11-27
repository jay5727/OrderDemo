package com.jay.orderdemo.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jay.orderdemo.R
import com.jay.orderdemo.api.ErrorHandling
import com.jay.orderdemo.api.Resource
import com.jay.orderdemo.base.DataBindingActivity
import com.jay.orderdemo.databinding.ActivityOrderBinding
import com.jay.orderdemo.extension.isInternetAvailable
import com.jay.orderdemo.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderActivity : DataBindingActivity() {

    lateinit var map: GoogleMap
    lateinit var mWifiReceiver: BroadcastReceiver
    private val binding: ActivityOrderBinding by binding(R.layout.activity_order)

    private val mViewModel by viewModels<OrderViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@OrderActivity
            viewModel = mViewModel
        }

        with(binding.mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync {
                //don't show marker route context menu,since FAB is on top of it
                it.uiSettings.isMapToolbarEnabled = false;
                map = it
                mViewModel.postOrderPage(1)
            }
        }
        attachObserver()
        setOnClickListener()
        setUpNetworkBroadcast()
    }

    /**
     * Method to attach observer
     */
    private fun attachObserver() {
        mViewModel.latLongLiveData.observe(
                this,
                androidx.lifecycle.Observer {
                    map?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                    it,
                                    1f//World zoom
                            )
                    )
                })

        mViewModel.orderListLiveData.observe(
                this,
                androidx.lifecycle.Observer { resource ->
                    when (resource.status) {
                        Resource.Status.LOADING -> {
                            mViewModel.shouldShowLoader.set(true)
                        }
                        Resource.Status.SUCCESS -> {
                            mViewModel.shouldShowLoader.set(false)
                            if (!resource.data?.orders.isNullOrEmpty()) {
                                val data = resource.data?.orders
                                //populate markers
                                data?.forEach { markerData ->
                                    markerData?.location?.let {
                                        map?.addMarker(
                                                MarkerOptions()
                                                        .position(LatLng(it.lat ?: 0.0, it.long
                                                                ?: 0.0))
                                                        .anchor(0.5f, 0.5f)
                                                        .title(markerData.address))
                                    }
                                }
                            }
                        }
                        Resource.Status.ERROR -> {
                            mViewModel.shouldShowLoader.set(false)
                            if (ErrorHandling.isNetworkError(resource.message ?: "")) {
                                Toast.makeText(
                                        this,
                                        ErrorHandling.ERROR_CHECK_NETWORK_CONNECTION,
                                        Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                        this,
                                        resource.message,
                                        Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
        )
    }

    /**
     * Method for overriding BroadcastReceiver
     */
    private fun setUpNetworkBroadcast() {
        mWifiReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (isInternetAvailable()) {
                    binding.btnOnline.setBackgroundColor(getColor(R.color.shade_green))
                    binding.btnOffline.setBackgroundColor(getColor(R.color.light_gray))
                } else {
                    binding.btnOffline.setBackgroundColor(getColor(R.color.red))
                    binding.btnOnline.setBackgroundColor(getColor(R.color.light_gray))
                }
            }
        }
    }

    /**
     * Method to register receiver
     */
    private fun registerWifiReceiver() {
        val filter = IntentFilter()
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(mWifiReceiver, filter)
    }

    /**
     * Method to unregister receiver
     */
    private fun unregisterWifiReceiver() {
        unregisterReceiver(mWifiReceiver)
    }

    /**
     * Method to set OnClick Listeners
     */
    private fun setOnClickListener() {
        binding.floatingActionButton.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment.newInstance()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    override fun onResume() {
        super.onResume()
        registerWifiReceiver()
        binding.mapView.onResume()

    }

    override fun onPause() {
        super.onPause()
        unregisterWifiReceiver()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

}