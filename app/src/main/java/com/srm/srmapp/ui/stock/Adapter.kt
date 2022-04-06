package com.srm.srmapp.ui.stock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srm.srmapp.R

class Adapter<B, T>(
    private var list: List<T>,
    private val bindSetup: (View) -> B,
    private val holderSetup: (B, T) -> Unit = { _, _ -> },
    private val onClick: (View, T) -> Unit = { _, _ -> },
) : RecyclerView.Adapter<Adapter.Holder<B, T>>() {

    class Holder<B, T>(view: View, bindSetup: (View) -> B) : RecyclerView.ViewHolder(view) {
        private var binding: B = bindSetup.invoke(view)
        fun bind(item: T, viewHolderSetup: (B, T) -> Unit) {
            viewHolderSetup.invoke(binding, item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder<B, T> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_stock, parent, false)
        return Holder(view, bindSetup)
    }

    override fun onBindViewHolder(holder: Holder<B, T>, position: Int) {
        val item = list[position]
        holder.bind(item, holderSetup)
        holder.itemView.setOnClickListener {
            onClick.invoke(it, item)
        }
    }

    override fun getItemCount(): Int = list.size

    fun find(predicate: (T) -> Boolean): Int {
        return list.indexOfFirst(predicate)
    }

    fun updateItems(other: List<T>) {
        list = other
        notifyDataSetChanged()
    }
}