package com.example.foca_mobile.activity.user.profile.profilesetting

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ListFavoriteItemBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Cart
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.service.CartService
import com.example.foca_mobile.service.ServiceGenerator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class FavoriteAdapter(private var c: Context, private val arrayList:List<Product>): Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(
        val binding:ListFavoriteItemBinding):RecyclerView.ViewHolder(binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val v = ListFavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(v)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        Glide.with(holder.binding.root.context)
            .load(arrayList[position].image)
            .into(holder.binding.imageFavorite)
        holder.binding.nameFavorite.text = arrayList[position].name
        holder.binding.description.text = arrayList[position].description
        val dec = DecimalFormat("#,###")
        holder.binding.price.text = dec.format(arrayList[position].price) + "đ"
        holder.binding.buyAgain.setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("productId",arrayList[position].id)
            jsonObject.put("quantity",1)
            val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            val createCartAgainAPI = ServiceGenerator.buildService(CartService::class.java).createCart(requestBody)
            createCartAgainAPI.enqueue(object: Callback<ApiResponse<Cart>> {
                override fun onResponse(
                    call: Call<ApiResponse<Cart>>,
                    response: Response<ApiResponse<Cart>>
                ) {
                    Toast.makeText(c, c.resources.getString(R.string.Addedtocart), Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<ApiResponse<Cart>>, t: Throwable) {}

            })
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}