package com.example.foca_mobile.activity.authen.forgotpass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityForgotPassScreenBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.service.AuthService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import kotlinx.android.synthetic.main.activity_forgot_pass_screen.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassScreen : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPassScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backBtn.setOnClickListener {
            this.finish()
        }
        sendBtn.setOnClickListener {
            toVerifyPassScreen()
        }
    }

    private fun toVerifyPassScreen() {

        val id: EditText = findViewById(R.id.idfield)
        if (id.text.toString().isEmpty()) {
            id.setBackgroundResource(R.drawable.rounded_edittext_error)
            return
        } else
            id.setBackgroundResource(R.drawable.rounded_edittext_normal)

        sendCodeFunc()
    }

    private fun sendCodeFunc() {
        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("username",  binding.idfield.text.toString())
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

                    val it = Intent(applicationContext, VerifiPassScreen::class.java)
                    it.putExtra("id",  binding.idfield.text.toString())
                    startActivity(it)
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