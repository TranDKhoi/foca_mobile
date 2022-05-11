package com.example.foca_mobile.activity.admin.order.orderdetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityAdminOrderDetailBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Notification
import com.example.foca_mobile.model.Order
import com.example.foca_mobile.model.OrderDetails
import com.example.foca_mobile.service.OrderService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.socket.SocketHandler
import com.example.foca_mobile.utils.ErrorUtils
import com.google.gson.Gson
import io.socket.client.Ack
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
    private var selectedStatus: MutableLiveData<String> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderDetailString = intent.getStringExtra("order")
        order = Gson().fromJson(orderDetailString, Order::class.java)
        orderDetailList = order.orderDetails!!
        selectedStatus.value = order.status!!

        binding.orderDetailRCV.adapter =
            AdminOrderDetailAdapter(orderDetailList)
        binding.orderDetailBack.setOnClickListener {
            this.finish()
        }
        binding.subTotal.text = NumberFormat.getCurrencyInstance().format(order.totalPrice)
        binding.priceTotal.text = NumberFormat.getCurrencyInstance().format(order.totalPrice)

        //INIT SPINNER
        initSpinner()

        binding.updateBtn.setOnClickListener {
            updateOrderStatus(selectedStatus.value.toString())
        }
        selectedStatus.observeForever {
            if (it == "PENDING") {
                binding.surcharge.visibility = EditText.VISIBLE
                binding.surchargeTxt.visibility = TextView.VISIBLE
            } else {
                binding.surcharge.visibility = EditText.GONE
                binding.surchargeTxt.visibility = TextView.GONE
                binding.priceTotal.text = order.totalPrice.toString()
            }

        }
        binding.surcharge.addTextChangedListener {
            if (!it.isNullOrEmpty())
                binding.priceTotal.text =
                    NumberFormat.getCurrencyInstance()
                        .format((order.totalPrice!! + it.toString().toInt()))

        }

    }

    private fun initSpinner() {
        when (order.status) {
            "ARRIVED" -> binding.spinner.text = resources.getString(R.string.ARRIVED)
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
                resources.getString(R.string.ARRIVED),
                resources.getString(R.string.PENDING),
                resources.getString(R.string.PROCESSED),
                resources.getString(R.string.COMPLETED),
                resources.getString(R.string.CANCELLED)
            )
            builder.setItems(status) { _, which ->
                when (which) {
                    0 -> {
                        selectedStatus.value = "ARRIVED"
                        binding.spinner.text = resources.getString(R.string.ARRIVED)
                    }
                    1 -> {
                        selectedStatus.value = "PENDING"
                        binding.spinner.text = resources.getString(R.string.PENDING)
                    }
                    2 -> {
                        selectedStatus.value = "PROCESSED"
                        binding.spinner.text = resources.getString(R.string.PROCESSED)
                    }
                    3 -> {
                        selectedStatus.value = "COMPLETED"
                        binding.spinner.text = resources.getString(R.string.COMPLETED)
                    }
                    4 -> {
                        selectedStatus.value = "CANCELLED"
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

        if (selectedStatus.value == "PENDING" && binding.surcharge.text.isEmpty()) {
            Toast.makeText(this, "Surcharge can not be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        if (selectedStatus.value == "PENDING") {
            jsonObject.put("status", status)
            jsonObject.put("surcharge", binding.surcharge.text)
        } else
            jsonObject.put("status", status)


        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        //CALL API
        val updateOrderCall = ServiceGenerator.buildService(OrderService::class.java)
            .updateOrderStatus(order.id.toString(), requestBody)

        updateOrderCall?.enqueue(object : Callback<ApiResponse<OrderDetails>> {
            override fun onResponse(
                call: Call<ApiResponse<OrderDetails>>,
                response: Response<ApiResponse<OrderDetails>>
            ) {
                Log.d("response.isSuccessful", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    kotlin.runCatching {
                        Toast.makeText(
                            applicationContext,
                            "Update Successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    val noti =  Notification(userId = order.buyerId, )

                    val yourOrderStr = resources.getString(R.string.YourOrder)
                    when(status){
                        "PENDING" ->{
                            noti.message = yourOrderStr + order.id +resources.getString(R.string.UPendingNoti)
                            noti.iconType = "MONEY"
                        }
                        "COMPLETED" ->{
                            noti.message = yourOrderStr + order.id +resources.getString(R.string.UCompletedNoti)
                            noti.iconType = "SUCCESS"
                        }
                        "CANCELLED" ->{
                            noti.message = yourOrderStr + order.id +resources.getString(R.string.UCancelledNoti)
                            noti.iconType = "CANCELLED"
                        }
                        "PROCESSED" ->{
                            noti.message = yourOrderStr + order.id +resources.getString(R.string.UProcessedNoti)
                            noti.iconType = "SUCCESS"
                        }
                        else -> {
                            return
                        }
                    }

                    SocketHandler.getSocket().emit("send_notification", Gson().toJson(noti))
                } else {
                    kotlin.runCatching {
                        val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                        Toast.makeText(
                            applicationContext,
                            errorRes.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<OrderDetails>>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Error Network!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    //HIDE KEYBOARD WHEN LOST FOCUS
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}