package com.example.foca_mobile.activity.admin.order.orderdetail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityAdminOrderDetailBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Order
import com.example.foca_mobile.model.OrderDetails
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
import java.text.NumberFormat

class AdminOrderDetail : AppCompatActivity() {

    private lateinit var binding: ActivityAdminOrderDetailBinding
    private lateinit var order: Order
    private lateinit var orderDetailList: MutableList<OrderDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderDetailString = intent.getStringExtra("order")
        order = Gson().fromJson(orderDetailString, Order::class.java)
        orderDetailList = order.orderDetails!!

        binding.orderDetailRCV.adapter =
            AdminOrderDetailAdapter(orderDetailList)
        binding.orderDetailBack.setOnClickListener {
            this.finish()
        }
        binding.priceTotal.text = NumberFormat.getCurrencyInstance().format(order.totalPrice)

        //INIT SPINNER
        initSpinner()


    }

    private fun initSpinner() {
        when (order.status) {
            "PENDING" -> binding.spinner.text = resources.getString(R.string.PENDING)
            "PROCESSED" -> binding.spinner.text = resources.getString(R.string.PROCESSED)
            "COMPLETED" -> binding.spinner.text = resources.getString(R.string.COMPLETED)
            "CANCELLED" -> binding.spinner.text = resources.getString(R.string.CANCELLED)
        }

        binding.spinner.setOnClickListener {
            // setup the alert builder
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setTitle(resources.getString(R.string.Filtertheorders))

            // add a list
            val status = arrayOf(
                resources.getString(R.string.PENDING),
                resources.getString(R.string.PROCESSED),
                resources.getString(R.string.COMPLETED),
                resources.getString(R.string.CANCELLED)
            )
            builder.setItems(status) { _, which ->
                when (which) {
                    0 -> {
                        updateOrderStatus("PENDING")
                        binding.spinner.text = resources.getString(R.string.PENDING)
                    }
                    1 -> {
                        updateOrderStatus("PROCESSED")
                        binding.spinner.text = resources.getString(R.string.PROCESSED)
                    }
                    2 -> {
                        updateOrderStatus("COMPLETED")
                        binding.spinner.text = resources.getString(R.string.COMPLETED)
                    }
                    3 -> {
                        updateOrderStatus("CANCELLED")
                        binding.spinner.text = resources.getString(R.string.CANCELLED)
                    }
                }
            }

            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun updateOrderStatus(status: String) {

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("status", status)
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        //CALL API
        val updateOrderCall = ServiceGenerator.buildService(OrderService::class.java)
            .updateOrderStatus(order.id.toString(), requestBody)

        updateOrderCall?.enqueue(object : Callback<ApiResponse<Int>> {

            override fun onResponse(
                call: Call<ApiResponse<Int>>,
                response: Response<ApiResponse<Int>>
            ) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        //RUN TOAST HERE
                    }
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Log.d("Error From Api", errorRes.message)
                }
            }

            override fun onFailure(call: Call<ApiResponse<Int>>, t: Throwable) {
            }
        })
        Toast.makeText(applicationContext, "Update Successfully!", Toast.LENGTH_SHORT).show()
    }
}