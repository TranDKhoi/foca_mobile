package com.example.foca_mobile.activity.admin.menu

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityFilterScreenBinding
import com.example.foca_mobile.utils.GlobalObject
import com.google.gson.Gson
import java.text.NumberFormat


class FilterScreen : AppCompatActivity() {

    private lateinit var binding: ActivityFilterScreenBinding
    private var isArrowUp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confirmButton.setOnClickListener {
            goBack()
        }
        binding.backBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            this.finish()
        }

        updateType(R.id.radio0)
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, _ ->
            updateType(radioGroup.checkedRadioButtonId)
        }

        updateSort(R.id.radio21)
        binding.radioGroup2.setOnCheckedChangeListener { radioGroup, _ ->
            updateSort(radioGroup.checkedRadioButtonId)
        }

        binding.radio3.setOnClickListener {

            if (!isArrowUp) {
                val rotation: Animation =
                    AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_arrow_up)
                it.startAnimation(rotation)
                rotation.fillAfter = true
                isArrowUp = true
                GlobalObject.filterData.way = true
            } else {
                val rotation: Animation =
                    AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_arrow_down)
                it.startAnimation(rotation)
                rotation.fillAfter = true
                isArrowUp = false
                GlobalObject.filterData.way = false
            }
        }

        //createSlider
        initSlider()
    }

    private fun initSlider() {
        val listPrice = mutableListOf(1f, 500000f)
        binding.priceRange.valueFrom = 0f
        binding.priceRange.valueTo = 500000f
        binding.priceRange.values = listPrice
        binding.priceRange.setLabelFormatter {
            NumberFormat.getCurrencyInstance().format(it)
        }
        GlobalObject.filterData.range.addAll(listPrice)
        binding.priceRange.addOnChangeListener { _, _, _ ->

            val values = binding.priceRange.values

            GlobalObject.filterData.range.clear()
            GlobalObject.filterData.range.addAll(values)
        }
    }

    private fun updateSort(i: Int) {
        binding.radio21.setBackgroundResource(R.drawable.radio_button_uncheck)
        binding.radio22.setBackgroundResource(R.drawable.radio_button_uncheck)

        val bt = findViewById<RadioButton>(i)
        bt.setBackgroundResource(R.drawable.radio_button_check)

        if (bt.text.toString() in "Rating".."Đánh giá")
            GlobalObject.filterData.sort = "rating"
        else if (bt.text.toString() in "Price".."Giá tiền")
            GlobalObject.filterData.sort = "price"
    }

    private fun updateType(i: Int) {

        binding.radio0.setBackgroundResource(R.drawable.radio_button_uncheck)
        binding.radio1.setBackgroundResource(R.drawable.radio_button_uncheck)
        binding.radio2.setBackgroundResource(R.drawable.radio_button_uncheck)

        val bt = findViewById<RadioButton>(i)
        bt.setBackgroundResource(R.drawable.radio_button_check)

        if (bt.text.toString() == "ALL") {
            GlobalObject.filterData.type = ""
        } else {
            if (bt.text.toString() in "FOOD".."ĐỒ ĂN")
                GlobalObject.filterData.type = "FOOD"
            else
                GlobalObject.filterData.type = "DRINK"
        }
    }

    private fun goBack() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        intent.putExtra("filterdata", Gson().toJson(GlobalObject.filterData))
        this.finish()
    }
}