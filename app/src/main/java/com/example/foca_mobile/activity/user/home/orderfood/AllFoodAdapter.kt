package com.example.foca_mobile.activity.user.home.orderfood

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.activity.user.home.infofood.InfoFood_Activity
import com.example.foca_mobile.databinding.ListAllRecentFoodItemBinding
import com.example.foca_mobile.model.Product
import java.text.DecimalFormat

class AllFoodAdapter(private var c: Context, private var arrList: MutableList<Product?>) :
    RecyclerView.Adapter<AllFoodAdapter.ViewHolder>(), Filterable {

    private var newArrayAddFoodListFilter = arrList

    inner class ViewHolder(val binding: ListAllRecentFoodItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            ListAllRecentFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.binding.root.context)
            .load(arrList[position]?.image)
            .into(holder.binding.imageFood)
        holder.binding.nameFood.text = arrList[position]?.name
        val dec = DecimalFormat("#,###")
        holder.binding.price.text = dec.format(arrList[position]?.price) + "đ"
        holder.binding.numberProduct.text = "Đã bán " + arrList[position]?.orderCount.toString()
        holder.binding.rBar.rating = arrList[position]?.averageRating!!.toFloat()
        holder.binding.root.setOnClickListener {
            val intent = Intent(c, InfoFood_Activity::class.java)
            intent.putExtra("id", arrList[position]?.id)
            c.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val filterResult = FilterResults()
                if (constraint == null || constraint.isEmpty()) {
                    filterResult.count = newArrayAddFoodListFilter.size
                    filterResult.values = newArrayAddFoodListFilter
                } else {
                    val searchCh: String = constraint.toString().toLowerCase()
                    val itemFoodList = ArrayList<Product>()
                    for (item in newArrayAddFoodListFilter) {
                        if (item?.name?.toLowerCase()?.contains(searchCh) == true ||
                            item?.description?.toLowerCase()?.contains(searchCh) == true
                        ) {
                            itemFoodList.add(item)
                        }
                    }
                    filterResult.count = itemFoodList.size
                    filterResult.values = itemFoodList
                }
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arrList = results!!.values as MutableList<Product?>
                notifyDataSetChanged()
            }
        }
    }
}