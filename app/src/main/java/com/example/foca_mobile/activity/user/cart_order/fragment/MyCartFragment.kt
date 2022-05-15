package com.example.foca_mobile.activity.user.cart_order.fragment

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
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


class MyCartFragment : Fragment() {
    private lateinit var binding: FragmentMyCartBinding
    private var listCart: MutableList<Cart>? = null
    private var adapter: RecyclerViewAdapterCart? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_my_cart, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMyCartBinding.bind(view)


//        val adapter = listCart?.let { RecyclerViewAdapterCart(it) }
//
//        binding.rvCart.layoutManager = LinearLayoutManager(activity)
//        binding.rvCart.adapter = adapter
//        binding.swipeRefreshLayout.setOnRefreshListener {
//            binding.swipeRefreshLayout.isRefreshing = false
//        }
        binding.cartButton.setOnClickListener {
            makeOrder()


        }
        setItemTouchHelper()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createCart()


        getListCart(this.context)
    }

    private fun createCart() {
        val jsonObject = JSONObject()
        jsonObject.put("productId", 3)
        jsonObject.put("quantity", 3)
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        val createCartCall = ServiceGenerator.buildService(CartService::class.java).createCart(requestBody)
        createCartCall.enqueue(object: Callback<ApiResponse<Cart>>{
            override fun onResponse(
                call: Call<ApiResponse<Cart>>,
                response: Response<ApiResponse<Cart>>
            ) {
                if(response.isSuccessful){
                Log.d("SUCCESS create cart", "YOLOOOOOOOOOOOOOOOOO")
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
        createOrderCall.enqueue(object : Callback<ApiResponse<Order>>{
            override fun onResponse(
                call: Call<ApiResponse<Order>>,
                response: Response<ApiResponse<Order>>
            ) {
                if(response.isSuccessful){
                    Log.d("SUCCESS create order", "YOLOOOOOOOOOOOOOOOOO")
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
        listCartCall.enqueue(object : Callback<ApiResponse<MutableList<Cart>>> {
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Cart>>>,
                response: Response<ApiResponse<MutableList<Cart>>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<MutableList<Cart>> = response.body()!!
                    listCart = res.data
                    adapter = RecyclerViewAdapterCart(listCart!!)
                    binding.rvCart.adapter = adapter
                    binding.rvCart.layoutManager = LinearLayoutManager(activity)

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

    private fun dipToPx(dipValue: Float, context: MyCartFragment): Int{
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }

}