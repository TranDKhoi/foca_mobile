package com.example.foca_mobile.activity.user.home.orderfood

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivitySearchScreenBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.service.ProductService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySearchScreenBinding
    private lateinit var searchString: String
    private lateinit var listSearch: MutableList<Product?>
    private lateinit var adapter: AllFoodAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listSearch = mutableListOf()
        adapter = AllFoodAdapter(this, listSearch)
        binding.SearchFoodRecyclerView.adapter = adapter

        searchString = intent.getStringExtra("searchstring").toString()

        binding.headerTitle.text =
            resources.getString(R.string.Searchresultfor).plus(searchString)

        callAPI()

        binding.buttonBack.setOnClickListener {
            this.finish()
        }
    }

    private fun callAPI() {
        //CALL API
        val loginCall = ServiceGenerator.buildService(ProductService::class.java)
            .getUserProductList(q = searchString)

        binding.progressBar.visibility = ProgressBar.VISIBLE
        loginCall?.enqueue(object : Callback<ApiResponse<MutableList<Product>>> {
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Product>>>,
                response: Response<ApiResponse<MutableList<Product>>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<MutableList<Product>> = response.body()!!
                    binding.progressBar.visibility = ProgressBar.GONE
                    listSearch.clear()
                    listSearch.addAll(res.data)
                    adapter = AllFoodAdapter(this@SearchScreen, listSearch)
                    binding.SearchFoodRecyclerView.adapter = adapter
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!);
                    Log.d("Error From Api", errorRes.message)
                    Toast.makeText(applicationContext, errorRes.message, Toast.LENGTH_LONG).show();
                }
                binding.progressBar.visibility = ProgressBar.GONE
            }

            override fun onFailure(call: Call<ApiResponse<MutableList<Product>>>, t: Throwable) {
            }
        })

    }
}