package com.srm.srmapp.ui.stock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srm.srmapp.R
import com.srm.srmapp.databinding.RvItemStockBinding

class Adapter<T>(
    private val list: List<T>,
    private val holderSetup: (RvItemStockBinding, T) -> Unit = { _, _ -> },
    private val onClick: (View, T) -> Unit = { _, _ -> },
) : RecyclerView.Adapter<Adapter.Holder<T>>() {
    class Holder<T>(view: View) : RecyclerView.ViewHolder(view) {
        private var binding = RvItemStockBinding.bind(view)
        fun bind(item: T, viewHolderSetup: (RvItemStockBinding, T) -> Unit) {
            viewHolderSetup.invoke(binding, item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder<T> {
        return Holder(LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item_stock, parent, false))
    }

    override fun onBindViewHolder(holder: Holder<T>, position: Int) {
        val item = list[position]
        holder.bind(item, holderSetup)
        holder.itemView.setOnClickListener {
            onClick.invoke(it, item)
        }
    }

    override fun getItemCount(): Int = list.size
}