package com.hallisanthe.hallisanthe

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

class ProductAdapter(private var list: List<Product>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.txtProductName)
        val artisan: TextView = v.findViewById(R.id.txtArtisanName)
        val village: TextView = v.findViewById(R.id.txtVillageName)
        val priceTag: TextView = v.findViewById(R.id.tvPriceTag)
        val img: ImageView = v.findViewById(R.id.imgProduct)
        val card: MaterialCardView = v as MaterialCardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = list[position]
        holder.name.text = p.name
        holder.artisan.text = "By ${p.artisanName}"
        holder.village.text = p.villageName
        holder.priceTag.text = "₹${p.price}"

        // Price-based color badges
        val context = holder.itemView.context
        val color = when {
            p.price <= 299 -> ContextCompat.getColor(context, R.color.budget_green)
            p.price <= 999 -> ContextCompat.getColor(context, R.color.medium_orange)
            else -> ContextCompat.getColor(context, R.color.premium_purple)
        }
        holder.priceTag.backgroundTintList = ColorStateList.valueOf(color)

        Glide.with(context)
            .load(p.imageUrl)
            .placeholder(R.drawable.gray_placeholder)
            .centerCrop()
            .into(holder.img)
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Product>) {
        list = newList
        notifyDataSetChanged()
    }
}