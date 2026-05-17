package com.hallisanthe.hallisanthe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private val categories: List<Category>, private val onItemClick: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val emoji: TextView = v.findViewById(R.id.tvCategoryEmoji)
        val name: TextView = v.findViewById(R.id.tvCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category_grid, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.emoji.text = category.emoji
        holder.name.text = category.name
        holder.itemView.setOnClickListener { onItemClick(category) }
    }

    override fun getItemCount() = categories.size
}