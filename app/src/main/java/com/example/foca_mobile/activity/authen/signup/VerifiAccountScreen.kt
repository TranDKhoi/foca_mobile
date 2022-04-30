package com.example.foca_mobile.activity.authen.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityVerifiAccountScreenBinding
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

class VerifiAccountScreen : AppCompatActivity() {

    private lateinit var UserName: String
    private lateinit var Password: String
    private lateinit var binding: ActivityVerifiAccountScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifiAccountScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UserName = intent.getStringExtra("id").toString();
        Password = intent.getStringExtra("pass").toString();

        val subText: TextView = findViewById(R.id.subText);
        subText.text = "Code send to $UserName@gm.uit.edu.vn";

        binding.confirmBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                confirmCode();
            }
        })
    }

    public fun toSignupScreen(view: View) {
        this.finish();
    }

    public fun toUploadBioScreen() {
        val it = Intent(this, BioScreen::class.java);
        it.putExtra("id", UserName)
        it.putExtra("pass", Password)
        finishAffinity();
        startActivity(it);
        this.finish();
    }

    public fun reSendCode(view: View) {
        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("username", UserName)
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        var sendCodeCall = ServiceGenerator.buildService(AuthService::class.java)
            .sendEmailVerification(requestBody)

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
                        .show();
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!);
                    Log.d("Error From Api", errorRes.message)
                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_LONG)
                        .show();
                }
                binding.bar.visibility = ProgressBar.GONE
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
            }

        })
    }

    public fun confirmCode() {

        val code: EditText = findViewById(R.id.codefield);
        if (code.text.toString().isNullOrEmpty() || code.text.toString().length < 4) {
            code.setBackgroundResource(R.drawable.rounded_edittext_error);
            return;
        } else
            code.setBackgroundResource(R.drawable.rounded_edittext_normal);

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("username", UserName)
        jsonObject.put("code", code.text.toString())
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        var sendCodeCall = ServiceGenerator.buildService(AuthService::class.java)
            .sendVerifyCode(requestBody)

        binding.bar.visibility = ProgressBar.VISIBLE
        sendCodeCall?.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    binding.bar.visibility = ProgressBar.GONE
                    toUploadBioScreen()
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!);
                    Log.d("Error From Api", errorRes.message)
                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_LONG)
                        .show();
                }
                binding.bar.visibility = ProgressBar.GONE
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
            }

        })
    }
}