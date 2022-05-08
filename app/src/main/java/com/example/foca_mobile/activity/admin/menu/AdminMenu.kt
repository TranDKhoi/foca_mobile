package com.example.foca_mobile.activity.admin.menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.FragmentAdminMenuBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.service.ProductService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdminMenu : Fragment() {

    private var _binding: FragmentAdminMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var myMenuList: MutableList<Product>
    private lateinit var myMenuAdapter: MyMenuAdapter
    private var selectedStatus: String = ""

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

        getMyMenu(selectedStatus)

        binding.createNewBtn.setOnClickListener {
            val intent = Intent(context, AdminCreateProduct::class.java)
            intent.putExtra("product", "")
            startActivity(intent)
        }

        //INIT SPINNER
        initSpinner()


        return binding.root
    }

    private fun initSpinner() {
        binding.spinner.text = resources.getString(R.string.ALL)
        binding.spinner.setOnClickListener {
            // setup the alert builder
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setTitle(resources.getString(R.string.Filterthemenu))

            // add a list
            val status = arrayOf(
                resources.getString(R.string.ALL),
                resources.getString(R.string.FOOD),
                resources.getString(R.string.DRINK)
            )
            builder.setItems(status) { _, which ->
                when (which) {
                    0 -> {
                        selectedStatus = ""
                        binding.spinner.text = resources.getString(R.string.ALL)
                        getMyMenu(selectedStatus)
                    }
                    1 -> {
                        selectedStatus = "FOOD"
                        binding.spinner.text = resources.getString(R.string.FOOD)
                        getMyMenu(selectedStatus)
                    }
                    2 -> {
                        selectedStatus = "DRINK"
                        binding.spinner.text = resources.getString(R.string.DRINK)
                        getMyMenu(selectedStatus)
                    }
                }
            }

            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun getMyMenu(type: String) {
        //CALL API
        val myMenuCall = ServiceGenerator.buildService(ProductService::class.java)
            .getProductList(type, 1000)
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