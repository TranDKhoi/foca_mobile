package com.example.foca_mobile.activity.user.cart_order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.foca_mobile.activity.user.cart_order.adapter.RecyclerViewAdapterOrderDetail
import com.example.foca_mobile.databinding.ActivityUserDetailOrderBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Order
import com.example.foca_mobile.model.Review
import com.example.foca_mobile.service.OrderService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat


class UserDetailOrder : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailOrderBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var order: Order = Order()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val orderId = intent.getIntExtra("orderid", 0)

        getOrderDetail(orderId.toString().toInt())

        binding.orderDetailBack.setOnClickListener { finish() }
        binding.cartButton.setOnClickListener {
            val intent = Intent(this@UserDetailOrder, UserRateScreen::class.java)
            val listReview: ArrayList<Review> = ArrayList()
            order.orderDetails!!.forEachIndexed { _, item ->
                val review = Review()
                review.orderDetail = item
                review.orderDetailId = item.id
                review.rating = 5
                listReview.add(review)
            }
            intent.putExtra("listReview", listReview)
            intent.putExtra("order", Gson().toJson(order))
            activityResultLauncher.launch(intent)
        }
        binding.orderDetailBtnDelete.setOnClickListener {
            deleteOrder(order)
            finish()
        }
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it!!.resultCode == Activity.RESULT_OK) {
                    hideReviewBtn()
                }
            }
    }

    private fun getOrderDetail(id: Int) {
        //CALL API
        val getCall = ServiceGenerator.buildService(OrderService::class.java)
            .getOrderDetail(id)

        getCall.enqueue(object : Callback<ApiResponse<Order>> {
            override fun onResponse(
                call: Call<ApiResponse<Order>>,
                response: Response<ApiResponse<Order>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<Order> = response.body()!!
                    order = res.data
                    val adapter = RecyclerViewAdapterOrderDetail(order.orderDetails!!)
                    val dec = DecimalFormat("#,###")
                    binding.totalPriceOrder.text = dec.format(order.totalPrice) + "đ"
                    binding.rvFood.adapter = adapter
                    if (order.status == "PROCESSED" || order.status == "COMPLETED") {
                        binding.orderDetailBtnDelete.visibility = View.GONE
                    }
                    if (order.isReviewed || order.status != "COMPLETED") {
                        hideReviewBtn()
                    }
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!);
                    Log.d("Error From Api", errorRes.message)
                }
            }

            override fun onFailure(call: Call<ApiResponse<Order>>, t: Throwable) {
            }
        })
    }

    private fun deleteOrder(item: Order) {
        val jsonObject = JSONObject()
        jsonObject.put("status", "COMPLETED")
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        val deleteCartItemCall =
            item.id?.let {
                ServiceGenerator.buildService(OrderService::class.java).deleteOrder(
                    requestBody,
                    it
                )
            }
        binding.bar.visibility = ProgressBar.VISIBLE
        deleteCartItemCall?.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    Log.d("SUCCESS delete order", "YOLO")
                    binding.bar.visibility = ProgressBar.GONE
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Log.d("Error From Api", errorRes.message)
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                Log.d("onFailure", "Call API failure")
            }

        })
    }

    private fun hideReviewBtn() {
        val recyclerView = binding.rvFood.layoutParams as ConstraintLayout.LayoutParams
        recyclerView.bottomToTop = binding.guideline3.id

        val params = binding.orderDetailCard.layoutParams as ConstraintLayout.LayoutParams
        params.topToBottom = binding.guideline3.id

        val linear1 = binding.linear1.layoutParams as ConstraintLayout.LayoutParams
        val linear2 = binding.linear2.layoutParams as ConstraintLayout.LayoutParams
        linear1.setMargins(50, 80, 0, 0)
        linear2.setMargins(0, 80, 50, 0)
        binding.cartButton.visibility = View.GONE
    }

}