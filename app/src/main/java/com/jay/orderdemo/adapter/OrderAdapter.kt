package com.jay.orderdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jay.orderdemo.databinding.ItemOrderBinding
import com.jay.orderdemo.listener.ItemClickListener
import com.jay.orderdemo.model.Order
import com.jay.orderdemo.viewmodel.OrderRowViewModel

/**
 * @param context object to access resources
 * @param orderList list containing all information for orders
 */
class OrderAdapter(
    private val context: Context,
    private val orderList: List<Order>
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    private var itemClickListener: ItemClickListener? = null
    private var checkedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.viewModel = OrderRowViewModel()

        binding.imageViewPhoneCall.setOnClickListener {
            binding.viewModel?.item?.get()?.let {
                itemClickListener?.onPhoneCallClicked(it)
            }
        }
        binding.imageViewNavigation.setOnClickListener {
            binding.viewModel?.item?.get()?.let {
                itemClickListener?.onNavigationClicked(it)
            }
        }
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderList[position], position)
    }

    /**
     * @param binding : binding file for Item Row Layout [ItemOrderBinding]
     */
    inner class ViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Order, position: Int) {
            binding.viewModel?.item?.set(item)
            binding.executePendingBindings()
            //set the item visibility
            if (checkedPosition == adapterPosition) {
                binding.btnDelivery.visibility = View.VISIBLE
            } else {
                binding.btnDelivery.visibility = View.GONE
            }

            binding.parentCard.setOnClickListener {
                binding.viewModel?.item?.get()?.let {
                    itemClickListener?.onItemClicked(adapterPosition, it)
                }
            }
        }
    }

    /**
     * @param position item position
     */
    fun setAdapterPosition(position: Int) {
        checkedPosition = position
        notifyDataSetChanged()
    }

    /**
     * Sets [ItemClickListener] for listening order item click.
     * @param clickListener Listener object
     */
    internal fun setOnItemClickListener(clickListener: ItemClickListener?) {
        itemClickListener = clickListener
    }
}