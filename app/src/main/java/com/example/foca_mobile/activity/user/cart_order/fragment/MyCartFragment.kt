package com.example.foca_mobile.activity.user.cart_order.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.activity.user.cart_order.adapter.RecyclerViewAdapterCart
import com.example.foca_mobile.databinding.FragmentMyCartBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Cart
import com.example.foca_mobile.model.Order
import com.example.foca_mobile.service.CartService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class MyCartFragment : Fragment(){
    private var _binding: FragmentMyCartBinding? = null
    private val binding get() = _binding!!
    private var listCart: MutableList<Cart>? = null
    private var adapter: RecyclerViewAdapterCart? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyCartBinding.inflate(inflater, container, false)
        binding.cartButton.setOnClickListener {
            makeOrder()
            listCart?.clear()
            val adapter = listCart?.let { RecyclerViewAdapterCart(it, this)}
            binding.rvCart.layoutManager = LinearLayoutManager(activity)
            binding.rvCart.adapter = adapter
            adapter?.notifyDataSetChanged()
        }
        setItemTouchHelper()
        createCart()
        getListCart(this.context)
        return binding.root
    }

    private fun createCart() {
        val jsonObject = JSONObject()
        jsonObject.put("productId", 1)
        jsonObject.put("quantity", 1)
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        val createCartCall = ServiceGenerator.buildService(CartService::class.java).createCart(requestBody)
        binding.bar.visibility = ProgressBar.VISIBLE
        createCartCall.enqueue(object: Callback<ApiResponse<Cart>>{
            override fun onResponse(
                call: Call<ApiResponse<Cart>>,
                response: Response<ApiResponse<Cart>>
            ) {
                if(response.isSuccessful){
                Log.d("SUCCESS create cart", "Yolo")
                    binding.bar.visibility = ProgressBar.GONE
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

    private fun makeOrder() {
        val jsonObject = JSONObject()
        jsonObject.put("notes", binding.cartNote.text)
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        val createOrderCall = ServiceGenerator.buildService(CartService::class.java).createOrder(
            requestBody = requestBody)
        binding.bar.visibility = ProgressBar.VISIBLE
        createOrderCall.enqueue(object : Callback<ApiResponse<Order>>{
            override fun onResponse(
                call: Call<ApiResponse<Order>>,
                response: Response<ApiResponse<Order>>
            ) {
                if(response.isSuccessful){
                    binding.totalPrice.text = "0đ"
                    Log.d("SUCCESS create order", "Yolo")
                    binding.bar.visibility = ProgressBar.GONE
                }
                else{
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Log.d("Error From Api", errorRes.message)
                }
            }
            override fun onFailure(call: Call<ApiResponse<Order>>, t: Throwable) {
                Log.d("onFailure","Call API failure")
            }
        })
    }

    private fun getListCart(context: Context?) {
        val listCartCall = ServiceGenerator.buildService(CartService::class.java).getUserCart()
        binding.bar.visibility = ProgressBar.VISIBLE
        listCartCall.enqueue(object : Callback<ApiResponse<MutableList<Cart>>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Cart>>>,
                response: Response<ApiResponse<MutableList<Cart>>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<MutableList<Cart>> = response.body()!!
                    listCart = res.data
                    adapter = RecyclerViewAdapterCart(listCart!!, this@MyCartFragment)
                    binding.rvCart.adapter = adapter
                    binding.rvCart.layoutManager = LinearLayoutManager(activity)
                    adapter!!.notifyDataSetChanged()
                    binding.bar.visibility = ProgressBar.GONE
                    calculatePrice(listCart, this@MyCartFragment)
                } else {
                    Toast.makeText(context, "Call api else error", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<ApiResponse<MutableList<Cart>>>, t: Throwable) {
                Toast.makeText(context, "Call api else error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setItemTouchHelper(){
        ItemTouchHelper(object : ItemTouchHelper.Callback(){
            private val limitScrollX = dipToPx(60f, this@MyCartFragment)
            private var currentScrollX = 0
            private var currentScrollWhenActive = 0
            private var initXWhenActive = 0f
            private var firstInActive = false
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }
            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return Integer.MAX_VALUE.toFloat()
            }
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if(actionState==ItemTouchHelper.ACTION_STATE_SWIPE){
                    if (dX==0f){
                        currentScrollX = viewHolder.itemView.scrollX
                        firstInActive=true
                    }
                    if (isCurrentlyActive){
                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if(scrollOffset > limitScrollX){
                            scrollOffset = limitScrollX
                        }
                        else if(scrollOffset<0){
                            scrollOffset = 0
                        }
                        viewHolder.itemView.scrollTo(scrollOffset, 0)
                    }
                    else{
                        if (firstInActive){
                            firstInActive = false
                            currentScrollWhenActive = viewHolder.itemView.scrollX
                            initXWhenActive = dX
                        }
                        if(viewHolder.itemView.scrollX<limitScrollX){
                            viewHolder.itemView.scrollTo((currentScrollWhenActive*dX/initXWhenActive).toInt(), 0)
                        }
                    }
                }
            }
            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                if (viewHolder.itemView.scrollX > limitScrollX){
                    viewHolder.itemView.scrollTo(limitScrollX, 0)
                }
                else if(viewHolder.itemView.scrollX < 0){
                    viewHolder.itemView.scrollTo(0, 0)
                }
            }
        }).apply {
            attachToRecyclerView(binding.rvCart)
        }
    }

    companion object Calculate{
        fun calculatePrice(listCart: MutableList<Cart>?, context: MyCartFragment){
            var totalPrice = 0
            for(item in listCart!!){
                totalPrice += item.quantity* item.product!!.price
            }
            val dec = DecimalFormat("#,###")
            context.binding.totalPrice.text = dec.format(totalPrice) + "đ"
        }
    }

    private fun dipToPx(dipValue: Float, context: MyCartFragment): Int{
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }


}

