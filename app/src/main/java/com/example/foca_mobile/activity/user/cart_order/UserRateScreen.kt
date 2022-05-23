package com.example.foca_mobile.activity.user.cart_order

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.cart_order.adapter.RecyclerViewAdapterRatingFood
import com.example.foca_mobile.databinding.ActivityUserRateScreenBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Order
import com.example.foca_mobile.model.Review
import com.example.foca_mobile.service.OrderService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class UserRateScreen : AppCompatActivity() {

    private lateinit var binding: ActivityUserRateScreenBinding
    private lateinit var listReview : ArrayList<Review>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRateScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSkip.setOnClickListener {
            finish()
        }
        intent.hasExtra("listReview").let {
            if (it) {
                val listType: Type =
                    object : TypeToken<ArrayList<Review>>() {}.type
                listReview = Gson().fromJson(
                    intent.getStringExtra("listReview"),
                    listType
                ) as ArrayList<Review>
            }
        }
        val order: Order =
            Gson().fromJson(intent.getStringExtra("order").toString(), Order::class.java)
        val adapter = RecyclerViewAdapterRatingFood(listReview)

        binding.rvRatingFood.adapter = adapter
        binding.rvRatingFood.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.submitBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(resources.getString(R.string.ReviewOrderMessage))
            builder.setPositiveButton(resources.getString(R.string.YES)) { dialog, _ -> //
                val jsonArray = JSONArray()
                val listReviewApi : ArrayList<Review> = ArrayList()
                listReview.forEachIndexed { _, item ->
                    val review = Review()
                    review.orderDetailId = item.orderDetailId
                    review.rating = item.rating
                    review.content = item.content
                    listReviewApi.add(review)
                    val jsonObject = JSONObject()
                    jsonObject.put("orderDetailId",item.orderDetailId)
                    jsonObject.put("rating",item.rating)
                    jsonObject.put("content",item.content)
                    jsonArray.put(jsonObject)
                }
                val jsonObject = JSONObject()
                jsonObject.put("reviews",jsonArray)
                val body: RequestBody = jsonObject.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaType())
                val createReviewCall =
                    order.id?.let { it1 ->
                        ServiceGenerator.buildService(OrderService::class.java).createReview(
                            body, it1
                        )
                    }
                binding.bar.visibility = ProgressBar.VISIBLE
                createReviewCall?.enqueue(object : Callback<ApiResponse<MutableList<Review>>> {
                    override fun onResponse(
                        call: Call<ApiResponse<MutableList<Review>>>,
                        response: Response<ApiResponse<MutableList<Review>>>
                    ) {
                        if(response.isSuccessful){
                            Log.d("SUCCESS","review success")
                            binding.bar.visibility = ProgressBar.GONE
                            goBack()
                        } else{
                            val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                            Log.d("Error From Api", errorRes.message)
                            Toast.makeText(applicationContext, response.message(), Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    override fun onFailure(call: Call<ApiResponse<MutableList<Review>>>, t: Throwable) {
                        Log.d("onFailure","Call API failure")
                    }
                })
                dialog.dismiss()
            }
            builder.setNegativeButton(
                resources.getString(R.string.NO)
            ) { dialog, _ -> // Do nothing
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.setOnShowListener {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
            }
            alert.show()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun goBack() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        this.finish()
    }
}


