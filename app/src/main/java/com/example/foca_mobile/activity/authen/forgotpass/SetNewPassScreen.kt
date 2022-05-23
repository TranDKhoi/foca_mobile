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
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.databinding.ActivitySetNewPassScreenBinding
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

class SetNewPassScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySetNewPassScreenBinding
    private lateinit var userName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetNewPassScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("id").toString()

        binding.backBtn.setOnClickListener {
            val intent = Intent(applicationContext, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }
        binding.finishbtn.setOnClickListener {
            toSuccessScreen()
        }
    }

    private fun toSuccessScreen() {

        val newPass: EditText = findViewById(R.id.newpass)
        val rePass: EditText = findViewById(R.id.repass)

        if (newPass.text.toString().isEmpty()) {
            newPass.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            newPass.setBackgroundResource(R.drawable.rounded_edittext_normal)
        if (rePass.text.toString().isEmpty()) {
            rePass.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            rePass.setBackgroundResource(R.drawable.rounded_edittext_normal)

        if (newPass.text.toString().isEmpty() || rePass.text.toString().isEmpty())
            return

        if (newPass.text.toString() != rePass.text.toString()) {
            Toast.makeText(this, resources.getString(R.string.Paswordnotmatch), Toast.LENGTH_LONG).show()
            return
        }

        callAPI()
    }

    private fun callAPI() {

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("username", userName)
        jsonObject.put("password", binding.repass.text.toString())
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val sendCodeCall = ServiceGenerator.buildService(AuthService::class.java)
            .resetPassword(requestBody)

        binding.bar.visibility = ProgressBar.VISIBLE
        sendCodeCall?.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    binding.bar.visibility = ProgressBar.GONE

                    val it = Intent(applicationContext, SuccessSetPassScreen::class.java)
                    finishAffinity()
                    startActivity(it)
                    finish()
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