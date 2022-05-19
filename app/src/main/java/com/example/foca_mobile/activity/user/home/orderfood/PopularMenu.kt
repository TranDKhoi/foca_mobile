package com.example.foca_mobile.activity.user.home.orderfood

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityUserPopularMenuBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.service.ProductService
import com.example.foca_mobile.service.ServiceGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularMenu : AppCompatActivity(){

    companion object {
        lateinit var appContext: Context
    }

    private lateinit var binding: ActivityUserPopularMenuBinding
    private lateinit var newArrayAddFoodList: MutableList<Product?>
    private lateinit var newArrayAddFoodListFilter: MutableList<Product?>
    private var strTypeFood: MutableList<String> = mutableListOf()
    private lateinit var adapterKT : AllFoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityUserPopularMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appContext = this
        createAddFoodRecyclerView()
        initSpinner()
        binding.buttonBack.setOnClickListener {
            this.finish()
        }
        binding.filterText.addTextChangedListener(object : TextWatcher{
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
            val builder = AlertDialog.Builder(appContext)
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
            binding.addFoodRecyclerView.adapter = AllFoodAdapter(appContext,newArrayAddFoodList)
        } else {
            val filteredList: List<Product?> = newArrayAddFoodList.filter { it?.type == filter }
            binding.addFoodRecyclerView.adapter =
                AllFoodAdapter(appContext,(filteredList) as MutableList)
        }
    }

    private fun createAddFoodRecyclerView() {
        binding.addFoodRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.addFoodRecyclerView.setHasFixedSize(true)

        newArrayAddFoodList = mutableListOf()
        newArrayAddFoodListFilter = mutableListOf()
        binding.progressBar.visibility = ProgressBar.VISIBLE
                val test = ServiceGenerator.buildService(ProductService::class.java).getUserProductList()
                test?.enqueue(object : Callback<ApiResponse<MutableList<Product>>> {
                    override fun onResponse(
                        call: Call<ApiResponse<MutableList<Product>>>,
                        response: Response<ApiResponse<MutableList<Product>>>
                    ) {
                        val res = response.body()!!
                        newArrayAddFoodList = res.data.toMutableList()
                        newArrayAddFoodListFilter.addAll(newArrayAddFoodList)
                        strTypeFood.add(getString(R.string.ALL))
                        newArrayAddFoodList.forEach {
                            if (!strTypeFood.any { s -> s == it?.type }) strTypeFood.add(it?.type!!)
                        }
                        adapterKT = AllFoodAdapter(appContext,newArrayAddFoodList)
                        binding.addFoodRecyclerView.adapter = adapterKT
                        binding.addFoodRecyclerView.apply {
                            layoutManager = GridLayoutManager(context, 2)
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<MutableList<Product>>>, t: Throwable) {}
                })
                binding.progressBar.visibility = ProgressBar.GONE
    }
}
