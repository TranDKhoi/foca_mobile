package com.example.foca_mobile.activity.user.cart_order

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.example.foca_mobile.databinding.ActivityUserRateScreenBinding

class UserRateScreen : AppCompatActivity() {

    private lateinit var binding: ActivityUserRateScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRateScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ratingBar.stepSize = 1f
        binding.btnSkip.setOnClickListener {
            finish()
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this!!.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}