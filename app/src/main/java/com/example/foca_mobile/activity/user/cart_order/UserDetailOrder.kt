package com.example.foca_mobile.activity.user.cart_order

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.activity.user.cart_order.adapter.RecyclerViewAdapterOrderDetail
import com.example.foca_mobile.databinding.ActivityUserDetailOrderBinding
import com.example.foca_mobile.model.Order
import com.example.foca_mobile.model.OrderDetails
import com.example.foca_mobile.model.Review


class UserDetailOrder : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val listOrderDetails : ArrayList<OrderDetails> = (intent.getSerializableExtra("listOrderDetails") as ArrayList<OrderDetails>?)!!
        val order : Order = intent.getSerializableExtra("order") as Order

        val temp : MutableList<OrderDetails> = listOrderDetails
        val adapter = RecyclerViewAdapterOrderDetail(temp)

        binding.rvFood.adapter = adapter
        binding.rvFood.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        if(order.isReviewed || order.status!="COMPLETED"){
            val params = binding.orderDetailCard.layoutParams as ConstraintLayout.LayoutParams
            params.topToBottom = binding.guideline3.id

            binding.cartButton.visibility = View.GONE
//            binding.orderDetailCard.setConstraintSet()
        }
        binding.orderDetailBack.setOnClickListener { finish() }
        binding.cartButton.setOnClickListener{
            val intent = Intent(this, UserRateScreen::class.java)
            val listReview : ArrayList<Review> = ArrayList()
            listOrderDetails.forEachIndexed { _, item ->
                val review = Review()
                review.orderDetail = item
                review.orderDetailId = item.id
                review.rating = 5
                listReview.add(review) }
            intent.putExtra("listReview", listReview)
            intent.putExtra("orderId", order.id)
            startActivity(intent)
        }
    }

}