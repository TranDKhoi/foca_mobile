package com.example.foca_mobile.activity.user.cart_order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.activity.user.cart_order.adapter.RecyclerViewAdapterRatingFood
import com.example.foca_mobile.databinding.ActivityUserRateScreenBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Review
import com.example.foca_mobile.service.OrderService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserRateScreen : AppCompatActivity() {

    private lateinit var binding: ActivityUserRateScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRateScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSkip.setOnClickListener {
            finish()
        }

        val listReview: ArrayList<Review> = intent.getSerializableExtra("listReview") as ArrayList<Review>
        val orderId : Int = intent.getIntExtra("orderId", -1)
        val temp : MutableList<Review> = listReview
        val adapter = RecyclerViewAdapterRatingFood(temp)

        binding.rvRatingFood.adapter = adapter
        binding.rvRatingFood.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.submitBtn.setOnClickListener {

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
//            val jsonObjectString = jsonObject.toString()
//            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            Log.d("JSONOBJECT", jsonObject.toString())
            val body: RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaType(),jsonObject.toString()
            )
            val createReviewCall = ServiceGenerator.buildService(OrderService::class.java).createReview(
                body, orderId)
            createReviewCall.enqueue(object : Callback<ApiResponse<MutableList<Review>>> {
                override fun onResponse(
                    call: Call<ApiResponse<MutableList<Review>>>,
                    response: Response<ApiResponse<MutableList<Review>>>
                ) {
                    if(response.isSuccessful){
                        Log.d("SUCCESS","YOLOOOOOOOOOOOOO")
//                        toMyOrderFragment()
                    }
                    else{
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
        }
    }

     fun toMyOrderFragment() {
        val it = Intent(this, UserMyCart::class.java)

        startActivity(it)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

}


