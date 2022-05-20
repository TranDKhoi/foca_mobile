package com.example.foca_mobile.activity.authen.forgotpass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityVerifiPassScreenBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.service.AuthService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifiPassScreen : AppCompatActivity() {

    private lateinit var binding: ActivityVerifiPassScreenBinding
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifiPassScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("id").toString()

        val subText: TextView = findViewById(R.id.subText)
        subText.text =
            resources.getString(R.string.Codesendto).plus("\n").plus(userName)
                .plus(resources.getString(R.string.gm_uit_edu_vn))


        binding.backBtn.setOnClickListener {
            this.finish()
        }
        binding.confirmButton.setOnClickListener {
            confirmCode()
        }

        binding.reSendCode.setOnClickListener {
            reSendCode()
        }
    }

    private fun toSetNewPassScreen() {
        val it = Intent(this, SetNewPassScreen::class.java)
        it.putExtra("id", userName)
        finishAffinity()
        startActivity(it)
        finish()
    }

    private fun confirmCode() {

        if (binding.codefield.text.toString()
                .isEmpty() || binding.codefield.text.toString().length < 4
        ) {
            binding.codefield.setBackgroundResource(R.drawable.rounded_edittext_error)
            return
        } else
            binding.codefield.setBackgroundResource(R.drawable.rounded_edittext_normal)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("username", userName)
        jsonObject.put("code", binding.codefield.text.toString())
        jsonObject.put("type", "RESET_PASSWORD")
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val sendCodeCall = ServiceGenerator.buildService(AuthService::class.java)
            .sendVerifyCode(requestBody)

        binding.bar.visibility = ProgressBar.VISIBLE
        sendCodeCall?.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    binding.bar.visibility = ProgressBar.GONE
                    toSetNewPassScreen()
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Log.d("Error From Api", errorRes.message)
                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_LONG)
                        .show()
                }
                binding.bar.visibility = ProgressBar.GONE
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
            }

        })
    }

    private fun reSendCode() {
        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("username", userName)
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val sendCodeCall = ServiceGenerator.buildService(AuthService::class.java)
            .sendEmailResetPassword(requestBody)

        binding.bar.visibility = ProgressBar.VISIBLE
        sendCodeCall?.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    binding.bar.visibility = ProgressBar.GONE
                    Toast.makeText(
                        applicationContext,
                        "We have resend the code!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Log.d("Error From Api", errorRes.message)
                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_LONG)
                        .show()
                }
                binding.bar.visibility = ProgressBar.GONE
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
            }

        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}