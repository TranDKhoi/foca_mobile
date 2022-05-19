package com.example.foca_mobile.activity.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.databinding.ActivityOnboardingScreenBinding


class OnboardingScreen : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingScreenBinding
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCurrentFragment1()
        binding.backBtn.setOnClickListener {
            backFunc()
        }
        binding.nextBtn.setOnClickListener {
            nextFunc()
        }

        binding.btnSkip.setOnClickListener {
            toLoginScreen()
        }
        binding.loginBtn.setOnClickListener {
            toLoginScreen()
        }
    }

    private fun nextFunc() {
        position++
        if (position != 0)
            binding.backBtn.visibility = TextView.VISIBLE
        if (position == 2) {
            binding.nextBtn.visibility = Button.GONE
            binding.loginBtn.visibility = Button.VISIBLE
        }
        setCurrentFragment1()
        updateIndicator()
    }

    private fun backFunc() {
        position--
        if (position == 0)
            binding.backBtn.visibility = TextView.GONE
        if (position != 2) {
            binding.nextBtn.visibility = Button.VISIBLE
            binding.loginBtn.visibility = Button.GONE
        }
        setCurrentFragment2()
        updateIndicator()
    }

    private fun setCurrentFragment1() =
        supportFragmentManager.beginTransaction().apply {
            var fragment = Fragment()
            when (position) {
                0 -> {
                    fragment = Boarding_1()
                }
                1 -> {
                    fragment = Boarding_2()
                }
                2 -> {
                    fragment = Boarding_3()
                }
            }
            setCustomAnimations(
                R.anim.enter_ltr,
                R.anim.exit_ltr,
                R.anim.enter_rtl,
                R.anim.exit_rtl
            )
            setCustomAnimations(
                R.anim.enter_rtl,
                R.anim.exit_rtl,
                R.anim.enter_ltr,
                R.anim.exit_ltr
            )
            addToBackStack(null)
            replace(R.id.wrapperB, fragment)
            commit()
        }

    private fun setCurrentFragment2() =
        supportFragmentManager.beginTransaction().apply {
            var fragment = Fragment()
            when (position) {
                0 -> {
                    fragment = Boarding_1()
                }
                1 -> {
                    fragment = Boarding_2()
                }
                2 -> {
                    fragment = Boarding_3()
                }
            }
            setCustomAnimations(
                R.anim.enter_ltr,
                R.anim.exit_ltr,
                R.anim.enter_rtl,
                R.anim.exit_rtl
            )

            addToBackStack(null)
            replace(R.id.wrapperB, fragment)
            commit()
        }

    private fun updateIndicator() {
        binding.radio0.setBackgroundResource(R.drawable.radio_button_uncheck)
        binding.radio1.setBackgroundResource(R.drawable.radio_button_uncheck)
        binding.radio2.setBackgroundResource(R.drawable.radio_button_uncheck)

        when (position) {
            0 -> {
                binding.radio0.setBackgroundResource(R.drawable.radio_button_check)
            }
            1 -> {
                binding.radio1.setBackgroundResource(R.drawable.radio_button_check)
            }
            2 -> {
                binding.radio2.setBackgroundResource(R.drawable.radio_button_check)
            }
        }
    }


    //SWIPE SCREEN
    private var x1 = 0f
    private var x2 = 0f
    val MIN_DISTANCE = 150
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> x1 = event.x
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                val deltaX: Float = x2 - x1
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // Left to Right swipe action
                    if (x2 > x1) {
                        if (position == 0)
                            return false
                        backFunc()
                    } else {
                        if (position == 2)
                            toLoginScreen()
                        nextFunc()
                    }
                } else {
                    // consider as something else - a screen tap for example
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun toLoginScreen() {
        val i = Intent(this, LoginScreen()::class.java)
        finishAffinity()
        startActivity(i)
        finish()
    }
}