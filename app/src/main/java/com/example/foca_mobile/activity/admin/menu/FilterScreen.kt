package com.example.foca_mobile.activity.admin.menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityFilterScreenBinding
import com.example.foca_mobile.model.Filter
import com.example.foca_mobile.utils.GlobalObject
import com.google.gson.Gson
import java.text.NumberFormat


class FilterScreen : AppCompatActivity() {

    private lateinit var binding: ActivityFilterScreenBinding
    private var isArrowDown = true
    private lateinit var filterData: Filter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        filterData = Gson().fromJson(intent.getStringExtra("filterdata"), Filter::class.java)

        binding.confirmButton.setOnClickListener {
            goBack()
        }
        binding.backBtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            GlobalObject.filterData = filterData
            this.finish()
        }

        when (GlobalObject.filterData.type) {
            "" -> updateType(R.id.radio0)
            "FOOD" -> updateType(R.id.radio1)
            "DRINK" -> updateType(R.id.radio2)
        }
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, _ ->
            updateType(radioGroup.checkedRadioButtonId)
        }
        Log.d("CHEKCK SORT", GlobalObject.filterData.sort!!)
        when (GlobalObject.filterData.sort) {
            "price" -> updateSort(R.id.radio21)
            "-price" -> updateSort(R.id.radio21)
            "rating" -> updateSort(R.id.radio22)
            "-rating" -> updateSort(R.id.radio22)
        }
        binding.radioGroup2.setOnCheckedChangeListener { radioGroup, _ ->
            updateSort(radioGroup.checkedRadioButtonId)
        }

        when (GlobalObject.filterData.wayUp!!) {
            true -> {
                val rotation: Animation =
                    AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_arrow_up)
                binding.radio3.startAnimation(rotation)
                rotation.fillAfter = true
                isArrowDown = false
            }
            false -> {
                val rotation: Animation =
                    AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_arrow_down)
                binding.radio3.startAnimation(rotation)
                rotation.fillAfter = true
                isArrowDown = true
            }
        }
        binding.radio3.setOnClickListener {

            if (isArrowDown) {
                val rotation: Animation =
                    AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_arrow_up)
                it.startAnimation(rotation)
                rotation.fillAfter = true
                isArrowDown = false
                GlobalObject.filterData.wayUp = true
            } else {
                val rotation: Animation =
                    AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_arrow_down)
                it.startAnimation(rotation)
                rotation.fillAfter = true
                isArrowDown = true
                GlobalObject.filterData.wayUp = false
            }
        }

        //createSlider
        initSlider()
    }

    private fun initSlider() {
        binding.priceRange.valueFrom = 0f
        binding.priceRange.valueTo = 500000f
        binding.priceRange.values.clear()
        binding.priceRange.values = GlobalObject.filterData.range
        binding.priceRange.setLabelFormatter {
            NumberFormat.getCurrencyInstance().format(it)
        }
        binding.priceRange.addOnChangeListener { _, _, _ ->

            val values = binding.priceRange.values

            GlobalObject.filterData.range.clear()
            GlobalObject.filterData.range.addAll(values)
        }
        binding.priceRange.stepSize = 1000f
    }

    private fun updateSort(i: Int) {
        binding.radio21.setBackgroundResource(R.drawable.radio_button_uncheck)
        binding.radio22.setBackgroundResource(R.drawable.radio_button_uncheck)

        val bt = findViewById<RadioButton>(i)
        bt.setBackgroundResource(R.drawable.radio_button_check)

        if (bt.id == binding.radio22.id)
            GlobalObject.filterData.sort = "rating"
        else if (bt.id == binding.radio21.id)
            GlobalObject.filterData.sort = "price"

    }

    private fun updateType(i: Int) {

        binding.radio0.setBackgroundResource(R.drawable.radio_button_uncheck)
        binding.radio1.setBackgroundResource(R.drawable.radio_button_uncheck)
        binding.radio2.setBackgroundResource(R.drawable.radio_button_uncheck)

        val bt = findViewById<RadioButton>(i)
        bt.setBackgroundResource(R.drawable.radio_button_check)

        if (bt.id == binding.radio0.id) {
            GlobalObject.filterData.type = ""
        } else {
            if (bt.id == binding.radio1.id)
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