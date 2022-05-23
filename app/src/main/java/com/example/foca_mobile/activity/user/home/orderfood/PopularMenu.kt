package com.example.foca_mobile.activity.user.home.orderfood

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.admin.menu.FilterScreen
import com.example.foca_mobile.activity.user.notifi.UserNotification
import com.example.foca_mobile.databinding.ActivityUserPopularMenuBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Filter
import com.example.foca_mobile.model.Notification
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.service.NotificationService
import com.example.foca_mobile.service.ProductService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.socket.SocketHandler
import com.example.foca_mobile.utils.ErrorUtils
import com.example.foca_mobile.utils.GlobalObject
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PopularMenu : AppCompatActivity() {

    companion object {
        lateinit var appContext: Context
    }

    private lateinit var binding: ActivityUserPopularMenuBinding
    private lateinit var newArrayAddFoodList: MutableList<Product?>
    private lateinit var newArrayAddFoodListFilter: MutableList<Product?>
    private lateinit var adapterKT: AllFoodAdapter
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityUserPopularMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appContext = this
        createAddFoodRecyclerView()
        getUnseenNotify()

        binding.buttonBack.setOnClickListener {
            this.finish()
        }
        binding.filterText.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })

        binding.notifyBtn.setOnClickListener {
            val intent = Intent(applicationContext, UserNotification::class.java)
            startActivity(intent)
        }

        //update badge notification
        val socket = SocketHandler.getSocket()
        socket.on("received_notification") {
            runOnUiThread {
                binding.notifyBtn.setImageResource(R.drawable.ic_notification_badge)
            }
        }

        binding.spinner.setOnClickListener {
            val intent = Intent(applicationContext, FilterScreen::class.java)
            intent.putExtra("filterdata", Gson().toJson(GlobalObject.filterData))
            activityResultLauncher.launch(intent)
        }
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it!!.resultCode == Activity.RESULT_OK) {
                    val filterData = it.data!!.dataString
                    createAddFoodRecyclerView(GlobalObject.filterData)
                }
            }
    }

    private fun performSearch() {
        val intent = Intent(this, SearchScreen::class.java)
        intent.putExtra("searchstring", binding.filterText.text.toString())
        startActivity(intent)
    }

    private fun createAddFoodRecyclerView(type: Filter? = null) {

        var test = ServiceGenerator.buildService(ProductService::class.java)
            .getUserProductList(limit = 1000)

        if (type != null) {
            if (!type.wayUp!!) {
                if (type.sort!![0] != '-')
                    type.sort = "-" + type.sort
            }
            if (type.wayUp!!) {
                if (type.sort!![0] == '-')
                    type.sort!!.removePrefix("-")
            }

            test = ServiceGenerator.buildService(ProductService::class.java)
                .getUserProductList(
                    limit = 1000,
                    price1 = type.range[0].toInt(),
                    price2 = type.range[1].toInt(),
                    type = type.type,
                    sort = type.sort
                )
        }

        binding.addFoodRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.addFoodRecyclerView.setHasFixedSize(true)

        newArrayAddFoodList = mutableListOf()
        newArrayAddFoodListFilter = mutableListOf()
        binding.progressBar.visibility = ProgressBar.VISIBLE

        test?.enqueue(object : Callback<ApiResponse<MutableList<Product>>> {
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Product>>>,
                response: Response<ApiResponse<MutableList<Product>>>
            ) {
                val res = response.body()!!
                newArrayAddFoodList = res.data.toMutableList()
                newArrayAddFoodListFilter.addAll(newArrayAddFoodList)
                adapterKT = AllFoodAdapter(appContext, newArrayAddFoodList)
                binding.addFoodRecyclerView.adapter = adapterKT
                binding.addFoodRecyclerView.apply {
                    layoutManager = GridLayoutManager(context, 2)
                }
            }

            override fun onFailure(call: Call<ApiResponse<MutableList<Product>>>, t: Throwable) {}
        })
        binding.progressBar.visibility = ProgressBar.GONE
    }

    private fun getUnseenNotify() {
        //CALL API
        val getNotificationCall = ServiceGenerator.buildService(NotificationService::class.java)
            .getUserNotify("false")

        getNotificationCall?.enqueue(object : Callback<ApiResponse<MutableList<Notification>>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Notification>>>,
                response: Response<ApiResponse<MutableList<Notification>>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<MutableList<Notification>> = response.body()!!

                    val listNotification: ArrayList<Int> = arrayListOf()
                    for (i in 0 until res.data.size) {
                        res.data[i].id?.let { listNotification.add(it) }
                    }
                    if (listNotification.size > 0)
                        binding.notifyBtn.setImageResource(R.drawable.ic_notification_badge)
                    else
                        binding.notifyBtn.setImageResource(R.drawable.ic_notification_non)

                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Toast.makeText(applicationContext, errorRes.message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<MutableList<Notification>>>,
                t: Throwable
            ) {
            }
        })
    }

    //HIDE KEYBOARD WHEN LOST FOCUS
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
