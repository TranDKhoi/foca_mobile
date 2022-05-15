package com.example.foca_mobile.activity.user.home.infofood

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityUserInfoFoodBinding
import com.example.foca_mobile.model.Product
import java.time.LocalDate


class InfoFood_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityUserInfoFoodBinding
    private lateinit var newArrayReviewFoodList: ArrayList<ReviewFood>
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolBar()
        initSpinner()
        binding.toolbar.setNavigationOnClickListener {
            this.finish()
        }
        product = intent.getSerializableExtra("Product") as Product
        initValueView()
        creatReviewRecycleview()
        binding.reviewRecycleview.adapter = ReviewFoodAdapter(newArrayReviewFoodList)
    }

    private fun initValueView() {
        Glide.with(binding.root.context)
            .load(product.image)
            .into(binding.imageFood)
        binding.nameFood.text = product.name
        binding.description.text = product.description
    }

    @SuppressLint("RestrictedApi")
    private fun initSpinner() {
        binding.addFood.setOnClickListener {
            val builder = AlertDialog.Builder(this, R.style.AlertDialog)
            builder.setTitle("Yêu cầu thêm đối với món ăn (nếu có)")
            val editText: EditText = EditText(this)
            editText.height = 300
            editText.gravity = Gravity.TOP
            editText.setBackgroundColor(Color.parseColor("#F6F1F1"))
            builder.setView(editText, 50, 30, 50, 0)
            builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                finish()
            })

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }
    }

    private fun creatReviewRecycleview() {
        binding.reviewRecycleview.layoutManager = LinearLayoutManager(this)
        binding.reviewRecycleview.setHasFixedSize(true)
        newArrayReviewFoodList = arrayListOf<ReviewFood>()
        for (i in 0..5) {
            newArrayReviewFoodList.add(
                ReviewFood(
                    R.drawable.favorite_6,
                    "Dianne Russell",
                    LocalDate.parse("2022-04-18"),
                    "This Is great, So delicious! You Must Here, With Your family . . ",
                    5
                )
            )
            newArrayReviewFoodList.add(
                ReviewFood(
                    R.drawable.favorite_7,
                    "Dianne Russell",
                    LocalDate.parse("2022-04-18"),
                    "This Is great, So delicious! You Must Here, With Your family . . ",
                    5
                )
            )
        }
    }
}