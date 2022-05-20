package com.example.foca_mobile.activity.user.home.infofood

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.databinding.ActivityUserReviewDetailsBinding
import com.example.foca_mobile.model.*
import com.example.foca_mobile.service.ProductService
import com.example.foca_mobile.service.ServiceGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class ReviewDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserReviewDetailsBinding
    private lateinit var newArrayAllReview : MutableList<Review>
    private lateinit var newArrayReviewStatistic: MutableList<ReviewStatistic>
    private var id by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserReviewDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getIntExtra("id",0)
        binding.View.visibility = View.GONE
        creatReviewRecycleview()
        initValueView()
        binding.buttonBack.setOnClickListener{
            this.finish()
        }
    }

    private fun creatReviewRecycleview() {
        binding.rcvReviewDetails.layoutManager = LinearLayoutManager(this)
        binding.rcvReviewDetails.setHasFixedSize(true)
        newArrayAllReview = mutableListOf()
        val getAllReviewAPI = ServiceGenerator.buildService(ProductService::class.java).getAllReview(id)
        getAllReviewAPI?.enqueue(object: Callback<ApiResponse<MutableList<Review>>> {
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Review>>>,
                response: Response<ApiResponse<MutableList<Review>>>
            ) {
                val res = response.body()!!
                newArrayAllReview = res.data
                binding.rcvReviewDetails.adapter = ReviewDetailsFoodAdapter(newArrayAllReview)
            }

            override fun onFailure(call: Call<ApiResponse<MutableList<Review>>>, t: Throwable) {}
        })
    }

    private fun initValueView() {
        newArrayReviewStatistic = mutableListOf()
        GlobalScope.launch(Dispatchers.IO) {
            val getReviewStatisticAPI = ServiceGenerator.buildService(ProductService::class.java).getReviewStatistic(id)
            getReviewStatisticAPI?.enqueue(object: Callback<ApiResponse<MutableList<ReviewStatistic>>> {
                override fun onResponse(
                    call: Call<ApiResponse<MutableList<ReviewStatistic>>>,
                    response: Response<ApiResponse<MutableList<ReviewStatistic>>>
                ) {
                    newArrayReviewStatistic = response.body()!!.data
                    var sumReview : Int = 0
                    var sumNumberStar : Int = 0
                    var num1 : Int = 0
                    var num2 : Int = 0
                    var num3 : Int = 0
                    var num4 : Int = 0
                    var num5 : Int = 0
                    newArrayReviewStatistic.forEach {
                        sumReview += it.quantity!!
                        sumNumberStar += it.rating!! * it.quantity!!
                        when(it.rating!!){
                            1 -> num1 += it.rating!! * it.quantity!!
                            2 -> num2 += it.rating!! * it.quantity!!
                            3 -> num3 += it.rating!! * it.quantity!!
                            4 -> num4 += it.rating!! * it.quantity!!
                            5 -> num5 += it.rating!! * it.quantity!!
                        }
                    }
                    binding.numReview.text = sumReview.toString()
                    binding.avgRating.text = (((sumNumberStar / sumReview) * 100.0).roundToInt() / 100.0).toString()
                    binding.rBar.rating = (((sumNumberStar / sumReview) * 100.0).roundToInt() / 100.0).toFloat()
                    binding.prg1.progress = num1 * 100 / sumNumberStar
                    binding.prg2.progress = num2 * 100 / sumNumberStar
                    binding.prg3.progress = num3 * 100 / sumNumberStar
                    binding.prg4.progress = num4 * 100 / sumNumberStar
                    binding.prg5.progress = num5 * 100 / sumNumberStar
                    binding.View.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<ApiResponse<MutableList<ReviewStatistic>>>,t: Throwable) {}
            })
        }

    }
}