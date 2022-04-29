package com.example.foca_mobile.activity.authen.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.MainActivity
import com.example.foca_mobile.activity.authen.forgotpass.ForgotPassScreen
import com.example.foca_mobile.activity.authen.signup.SignupScreen
import com.example.foca_mobile.databinding.ActivityLoginScreenBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.User
import com.example.foca_mobile.service.AuthService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import com.example.foca_mobile.utils.LoginPrefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginScreen : AppCompatActivity() {

    companion object {
        lateinit var appContext: Context
    }

    private lateinit var binding: ActivityLoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appContext = this

        val token = LoginPrefs.getUserToken()
        if (token != "")
            verifyAccessTokenFunc("Bearer $token")
    }


    public fun toSignupScreen(view: View) {
        val intent = Intent(this, SignupScreen::class.java);

        startActivity(intent);
    }

    public fun toForgotPassScreen(view: View) {
        val intent = Intent(this, ForgotPassScreen::class.java);

        startActivity(intent);
    }

    public fun loginFunc(view: View) {
        val id: EditText = binding.idfield;
        val pas: EditText = binding.passfield;

        //CHECK DATA
        if (id.text.toString().isEmpty()) {
            id.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else
            id.setBackgroundResource(R.drawable.rounded_edittext_normal);

        if (pas.text.toString().isEmpty()) {
            pas.setBackgroundResource(R.drawable.rounded_edittext_error);
        } else
            pas.setBackgroundResource(R.drawable.rounded_edittext_normal);

        if (id.text.toString().isEmpty() || pas.text.toString().isEmpty())
            return;

        binding.progressBar.visibility = ProgressBar.VISIBLE
        //CALL API
        val loginCall = ServiceGenerator.buildService(AuthService::class.java)
            .login(User(username = id.text.toString(), password = pas.text.toString()));

        loginCall?.enqueue(object : Callback<ApiResponse<User>> {
            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<User> = response.body()!!
                    val user = res.data
                    LoginPrefs.setUserToken(user.accessToken!!)
                    binding.progressBar.visibility = ProgressBar.GONE
                    directLogin(user)
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!);
                    Log.d("Error From Api", errorRes.message)
                    Toast.makeText(applicationContext, errorRes.message, Toast.LENGTH_LONG).show();
                }
                binding.progressBar.visibility = ProgressBar.GONE
            }

            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
            }
        })
    }

    public fun directLogin(user: User) {
        val it = Intent(this, MainActivity::class.java);
        it.putExtra("currentUser", user);
        startActivity(it);
        this.finish();
    }

    private fun verifyAccessTokenFunc(token: String) {

        //CALL API
        val loginCall = ServiceGenerator.buildService(AuthService::class.java)
            .verifyAccessToken(token);

        binding.progressBar.visibility = ProgressBar.VISIBLE
        loginCall?.enqueue(object : Callback<ApiResponse<User>> {
            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<User> = response.body()!!
                    val user = res.data
                    binding.progressBar.visibility = ProgressBar.GONE
                    directLogin(user)
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!);
                    Log.d("Error From Api", errorRes.message)
                    Toast.makeText(applicationContext, errorRes.message, Toast.LENGTH_LONG).show();
                }
                binding.progressBar.visibility = ProgressBar.GONE
            }

            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
            }
        })


    }
}