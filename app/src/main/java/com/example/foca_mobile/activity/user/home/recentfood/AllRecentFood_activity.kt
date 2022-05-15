package com.example.foca_mobile.activity.user.home.recentfood

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityUserAllRecentFoodBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Order
import com.example.foca_mobile.model.OrderDetails
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.service.OrderService
import com.example.foca_mobile.service.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllRecentFood_activity : AppCompatActivity() {

    companion object {
        lateinit var appContext: Context
    }

    private lateinit var binding: ActivityUserAllRecentFoodBinding
    private lateinit var newArrayRecentFoodList: MutableList<Product?>
    private var strTypeFood: MutableList<String> = mutableListOf()
    private lateinit var adapterKT : AllRecentFoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityUserAllRecentFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appContext = this
        createAddFoodRecyclerView()
        initSpinner()
        binding.buttonBack.setOnClickListener {
            this.finish()
        }
        binding.filterText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                adapterKT.filter.filter(s)
            }
        })
    }

    private fun initSpinner() {
        binding.spinner.setOnClickListener {
            // setup the alert builder
            val builder = AlertDialog.Builder(this)
            builder.setTitle(resources.getString(R.string.Filterthemenu))
            val status : Array<String> = strTypeFood.map { it }.toTypedArray()
            builder.setItems(status) { _, item ->
                notifyAddFoodRecyclerView(strTypeFood[item])
            }

            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun notifyAddFoodRecyclerView(filter: String) {
        if (filter == getString(R.string.ALL)) {
            binding.allRecentFoodRecycleView.adapter = AllRecentFoodAdapter(newArrayRecentFoodList)
        } else {
            val filteredList: List<Product?> = newArrayRecentFoodList.filter { it?.type == filter }
            binding.allRecentFoodRecycleView.adapter =
                AllRecentFoodAdapter((filteredList) as MutableList)
        }
    }

    private fun createAddFoodRecyclerView() {
        binding.allRecentFoodRecycleView.layoutManager = LinearLayoutManager(this)
        binding.allRecentFoodRecycleView.setHasFixedSize(true)

        newArrayRecentFoodList = mutableListOf()
        val recentFoodApi = ServiceGenerator.buildService(OrderService::class.java).getRecentOrderList()
        recentFoodApi?.enqueue(object : Callback<ApiResponse<MutableList<Order>>> {
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Order>>>,
                response: Response<ApiResponse<MutableList<Order>>>
            ) {
                val res = response.body()!!
                val tempOrderDetailsList1 : MutableList<OrderDetails> = mutableListOf()
                res.data.forEach {
                    it.orderDetails?.let { it1 -> tempOrderDetailsList1.addAll(it1) }
                }
                for (it in tempOrderDetailsList1) {
                    if (!newArrayRecentFoodList.any { pd -> pd?.id == it.productId }) newArrayRecentFoodList.add(it.product)
                }
                strTypeFood.add(getString(R.string.ALL))
                newArrayRecentFoodList.forEach {
                    if (!strTypeFood.any { s -> s == it?.type }) strTypeFood.add(it?.type!!)
                }
                adapterKT = AllRecentFoodAdapter(newArrayRecentFoodList)
                binding.allRecentFoodRecycleView.adapter = adapterKT
                binding.allRecentFoodRecycleView.apply {
                    layoutManager = GridLayoutManager(context,2)
                }
            }

            override fun onFailure(call: Call<ApiResponse<MutableList<Order>>>, t: Throwable) {}
        })
    }
}