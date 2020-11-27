package com.jay.orderdemo.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jay.orderdemo.R
import com.jay.orderdemo.adapter.OrderAdapter
import com.jay.orderdemo.api.Resource
import com.jay.orderdemo.databinding.BottomSheetFragmentBinding
import com.jay.orderdemo.listener.ItemClickListener
import com.jay.orderdemo.model.Order
import com.jay.orderdemo.viewmodel.OrderViewModel
import java.util.*

/**
 * Bottom Sheet Fragment displaying Order Info List inherits [BottomSheetDialogFragment]
 */
class BottomSheetFragment : BottomSheetDialogFragment(), ItemClickListener {

    companion object {
        val TAG = BottomSheetFragment::class.java
        fun newInstance() = BottomSheetFragment()
    }

    private lateinit var binding: BottomSheetFragmentBinding
    private lateinit var adapter: OrderAdapter
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        attachObserver()
    }

    /**
     * Method to attach observer
     */
    private fun attachObserver() {
        viewModel.orderListLiveData.observe(
            this,
            androidx.lifecycle.Observer { resource ->
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        if (!resource.data?.orders.isNullOrEmpty()) {
                            adapter = OrderAdapter(
                                context = requireContext(),
                                orderList = resource.data?.orders?.toMutableList() as ArrayList<Order>
                            )
                            binding.adapter = adapter
                            adapter?.setOnItemClickListener(this)
                        }
                    }
                    else -> {
                        // no-op
                    }
                }
            }
        )
    }

    override fun onItemClicked(position: Int, order: Order?) {
        adapter?.setAdapterPosition(position)
        viewModel.latLongLiveData.postValue(
            LatLng(
                order?.location?.lat ?: 0.0,
                order?.location?.long ?: 0.0
            )
        )
    }

    override fun onPhoneCallClicked(order: Order?) {
        order?.phone?.let {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${it}")
            startActivity(intent)
        } ?: run {
            Toast.makeText(
                requireActivity(),
                getString(R.string.no_phone_number_found),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onNavigationClicked(order: Order?) {
        order?.location?.let {
            //val uri: String = java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", 18.9067, 72.8147)
            val uri: String = java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", it.lat, it.long)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            activity?.startActivity(intent)
        } ?: run {
            Toast.makeText(
                requireActivity(),
                getString(R.string.no_loc_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        adapter?.setOnItemClickListener(null)
        super.onDestroy()
    }

}