package com.example.foca_mobile.activity.user.notifi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.databinding.ActivityUserNotificationBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Notification
import com.example.foca_mobile.service.NotificationService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserNotification : AppCompatActivity() {

    private lateinit var binding: ActivityUserNotificationBinding
    private lateinit var notifyList: MutableList<Notification>
    private lateinit var notifyAdapter: UserNotificationAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notifyList = mutableListOf()
        notifyAdapter = UserNotificationAdapter(notifyList)
        binding.notifyRCV.adapter = notifyAdapter

        //CALL API
        getNotify()

        binding.backBtn.setOnClickListener { this.finish() }

    }

    private fun getNotify() {
        //CALL API
        var getNotificationCall = ServiceGenerator.buildService(NotificationService::class.java)
            .getUserNotify()

        binding.bar.visibility = ProgressBar.VISIBLE
        getNotificationCall?.enqueue(object : Callback<ApiResponse<MutableList<Notification>>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Notification>>>,
                response: Response<ApiResponse<MutableList<Notification>>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<MutableList<Notification>> = response.body()!!
                    notifyList = res.data
                    notifyAdapter = UserNotificationAdapter(notifyList)
                    binding.notifyRCV.adapter = notifyAdapter
                    binding.bar.visibility = ProgressBar.GONE
                    notifyAdapter.notifyDataSetChanged()
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!);
                    Log.d("Error From Api", errorRes.message)
                    Toast.makeText(applicationContext, errorRes.message, Toast.LENGTH_LONG).show();
                }
                binding.bar.visibility = ProgressBar.GONE
            }

            override fun onFailure(
                call: Call<ApiResponse<MutableList<Notification>>>,
                t: Throwable
            ) {
            }
        })
    }
}