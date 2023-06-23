package com.example.swipeassignment.adapter.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.swipeassignment.R
import com.example.swipeassignment.data.models.ProductDataDataModel
import com.example.swipeassignment.databinding.ProductRvItemBinding

class ProductListRVAdapter(private val productList: List<ProductDataDataModel>):
    RecyclerView.Adapter<ProductListRVAdapter.ProductListItemViewHolder>() {

    inner class ProductListItemViewHolder(binding: ProductRvItemBinding): RecyclerView.ViewHolder(binding.root) {
        val productNameTV = binding.productNameTv
        val productPriceTV = binding.productPriceTv
        val productTaxTV = binding.productTaxTv
        val productImageIV = binding.productImageIv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListItemViewHolder {
        return ProductListItemViewHolder(ProductRvItemBinding.inflate(LayoutInflater
            .from(parent.context), null, false))
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductListItemViewHolder, position: Int) {
        holder.apply {
            val productData = productList[position]
            if (productData.image.isNotEmpty()) {
                productImageIV.load(productData.image) {
                    placeholder(R.drawable.placeholder_image_icon)
                }
            } else {
                productImageIV.setImageDrawable(ResourcesCompat.
                getDrawable(holder.itemView.resources, R.drawable.placeholder_image_icon, holder.itemView.context.theme))
            }
            productNameTV.text = "%s %s".format(productData.productName, productData.productType)
            productPriceTV.text = String.format("%.2f", productData.price)
            productTaxTV.text = String.format("%.2f", productData.tax)
            holder.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }
}