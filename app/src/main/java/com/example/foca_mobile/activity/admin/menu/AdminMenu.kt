package com.example.foca_mobile.activity.admin.menu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.foca_mobile.databinding.FragmentAdminMenuBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Filter
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.service.ProductService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import com.example.foca_mobile.utils.GlobalObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdminMenu : Fragment() {

    private var _binding: FragmentAdminMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var myMenuList: MutableList<Product>
    private lateinit var myMenuAdapter: MyMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAdminMenuBinding.inflate(inflater, container, false)

        //INIT RCV
        myMenuList = arrayListOf()
        myMenuAdapter = MyMenuAdapter(myMenuList)
        binding.menuRCV.adapter = myMenuAdapter

        getMyMenu()

        binding.createNewBtn.setOnClickListener {
            val intent = Intent(context, AdminCreateProduct::class.java)
            intent.putExtra("product", "")
            startActivity(intent)
        }

        binding.filterBtn.setOnClickListener {
            val intent = Intent(context, FilterScreen::class.java)
            activityResultLauncher.launch(intent)
        }
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it!!.resultCode == Activity.RESULT_OK) {
                    Log.d("CHECK DATA", GlobalObject.filterData.toString())
                }
            }

        return binding.root
    }

    private fun getMyMenu(type: Filter? = null) {
        //CALL API
        val myMenuCall = ServiceGenerator.buildService(ProductService::class.java)
            .getProductList(limit = 1000)
        binding.bar.visibility = ProgressBar.VISIBLE
        myMenuCall?.enqueue(object : Callback<ApiResponse<MutableList<Product>>> {
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Product>>>,
                response: Response<ApiResponse<MutableList<Product>>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<MutableList<Product>> = response.body()!!
                    myMenuList = res.data
                    binding.menuRCV.adapter =
                        MyMenuAdapter(myMenuList)
                    binding.bar.visibility = ProgressBar.GONE
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                    Log.d("Error From Api", errorRes.message)
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<MutableList<Product>>>,
                t: Throwable
            ) {
            }
        })
    }
}