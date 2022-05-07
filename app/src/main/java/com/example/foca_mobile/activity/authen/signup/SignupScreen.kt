package com.example.foca_mobile.activity.authen.signup

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
import com.example.foca_mobile.databinding.ActivitySignupScreenBinding
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

class SignupScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySignupScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAccountBtn.setOnClickListener {
            toVerifyScreen()
        }
        binding.backBtn.setOnClickListener { this.finish() }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun toVerifyScreen() {

        val id: EditText = findViewById(R.id.idfield)
        val pass: EditText = findViewById(R.id.passfield)
        val rePass: EditText = findViewById(R.id.repass)

        if (id.text.toString().isEmpty()) {
            id.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            id.setBackgroundResource(R.drawable.rounded_edittext_normal)

        if (pass.text.toString().isEmpty()) {
            pass.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            pass.setBackgroundResource(R.drawable.rounded_edittext_normal)

        if (rePass.text.toString().isEmpty()) {
            rePass.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            rePass.setBackgroundResource(R.drawable.rounded_edittext_normal)

        if (pass.text.toString().length < 6) {
            pass.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            pass.setBackgroundResource(R.drawable.rounded_edittext_normal)

        if (rePass.text.toString().length < 6) {
            rePass.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            rePass.setBackgroundResource(R.drawable.rounded_edittext_normal)

        if (id.text.toString().isEmpty() ||
            pass.text.toString().isEmpty() ||
            rePass.text.toString().isEmpty()
        )
            return

        if (pass.text.toString() != rePass.text.toString()) {
            Toast.makeText(this, "The password does not match!", Toast.LENGTH_LONG).show()
            return
        }
        sendCodeFunc()

    }

    private fun sendCodeFunc() {
        val id: EditText = binding.idfield
        val rePass: EditText = binding.repass


        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("username", id.text.toString())
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val sendCodeCall = ServiceGenerator.buildService(AuthService::class.java)
            .sendEmailVerification(requestBody)

        binding.bar.visibility = ProgressBar.VISIBLE
        sendCodeCall?.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    binding.bar.visibility = ProgressBar.GONE
                    val it = Intent(applicationContext, VerifiAccountScreen::class.java)
                    it.putExtra("id", id.text.toString())
                    it.putExtra("pass", rePass.text.toString())
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
}