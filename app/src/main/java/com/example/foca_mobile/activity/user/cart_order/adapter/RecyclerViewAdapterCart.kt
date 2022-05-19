package com.example.foca_mobile.activity.user.cart_order.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.cart_order.adapter.item.CartItemViewHolder
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Cart
import com.example.foca_mobile.service.CartService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerViewAdapterCart(private val listProduct: MutableList<Cart>) :
    RecyclerView.Adapter<CartItemViewHolder>() {
//    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var name: TextView = itemView.findViewById(R.id.cartItemName)
//        var option: TextView = itemView.findViewById(R.id.cartItemOption)
//        var price: TextView = itemView.findViewById(R.id.cartItemPrice)
//        var quantity: TextView = itemView.findViewById(R.id.cartItemQuantity)
//        var img: ImageView = itemView.findViewById(R.id.cartItemImage)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_cart_item, parent, false)
        return CartItemViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val item = listProduct[position]

        Glide.with(holder.itemView.context)
            .load(item.product?.image)
            .into(holder.img)

        holder.name.text = item.product?.name
        holder.option.text = item.product?.description
        holder.price.text = item.product?.price.toString()


        holder.quantity.text = item.quantity.toString()

        holder.onDeleteClick = {
            deleteCartItem(item.productId, item.quantity, item.id)
            removeItem(it as CartItemViewHolder)

        }
        holder.updateView()
        holder.subQuantity.setOnClickListener {
            if (listProduct[position].quantity > 1) {
                updateCartItem(item, true)
                notifyDataSetChanged()
            }

        }
        holder.addQuantity.setOnClickListener {

            updateCartItem(item, false)
            notifyDataSetChanged()
        }
    }


    private fun updateCartItem(item: Cart, isAdd: Boolean) {
        if (isAdd) item.quantity-- else item.quantity++
        val jsonObject = JSONObject()
        jsonObject.put("quantity", item.quantity)
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        val deleteCartItemCall = ServiceGenerator.buildService(CartService::class.java).updateCart( requestBody ,item.id)

        deleteCartItemCall.enqueue(object: Callback<ApiResponse<Cart>>{
            override fun onResponse(
                call: Call<ApiResponse<Cart>>,
                response: Response<ApiResponse<Cart>>
            ) {
                if(response.isSuccessful){
                    Log.d("SUCCESS update cart", "YOLO")
                }
                else{
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Log.d("Error From Api", errorRes.message)
                }
            }

            override fun onFailure(call: Call<ApiResponse<Cart>>, t: Throwable) {
                Log.d("onFailure","Call API failure")
            }

        })
    }

    private fun deleteCartItem(productId: Int, quantity: Int, cartId: Int) {
        val jsonObject = JSONObject()
        jsonObject.put("productId", productId)
        jsonObject.put("quantity", quantity)
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        val deleteCartItemCall = ServiceGenerator.buildService(CartService::class.java).deleteCart(requestBody,cartId)

        deleteCartItemCall.enqueue(object: Callback<ApiResponse<Int>>{
            override fun onResponse(
                call: Call<ApiResponse<Int>>,
                response: Response<ApiResponse<Int>>
            ) {
                if(response.isSuccessful){
                    Log.d("SUCCESS delete cart", "YOLO")
                }
                else{
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Log.d("Error From Api", errorRes.message)
                }
            }

            override fun onFailure(call: Call<ApiResponse<Int>>, t: Throwable) {
                Log.d("onFailure","Call API failure")
            }

        })
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    private fun removeItem(viewHolder: CartItemViewHolder) {
        listProduct.removeAt(viewHolder.bindingAdapterPosition)
        notifyItemRemoved(viewHolder.bindingAdapterPosition)
    }


}