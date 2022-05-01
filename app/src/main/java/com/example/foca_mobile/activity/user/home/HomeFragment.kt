package com.example.foca_mobile.activity.user.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.model.User
import com.example.foca_mobile.service.AuthService
import com.example.foca_mobile.service.ProductService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(
            R.layout.fragment_home, container,
            false
        )

        var fetchPostBtn = view.findViewById<Button>(R.id.fetch_post_btn)
        var progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)

        fetchPostBtn.setOnClickListener {
            var loginCall = ServiceGenerator.buildService(AuthService::class.java).login(User(username = "20521161", password = "123456"));

            loginCall?.enqueue(object : Callback<ApiResponse<User>>{
                override fun onResponse(
                    call: Call<ApiResponse<User>>,
                    response: Response<ApiResponse<User>>
                ) {
                    if(response.isSuccessful){
                        val res: ApiResponse<User> = response.body()!!
                        val user = res.data
                        Log.d("Access Token", user.accessToken!! )
                    }
                    else{
                        val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!);
                        Log.d("Error From Api", errorRes.message )
                    }
                }

                override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                }

            })


            var getPostCall = ServiceGenerator.buildService(ProductService::class.java).getProductList();
            progressBar.visibility = ProgressBar.VISIBLE

            Log.d("Main Activity", "before call api")
            getPostCall.enqueue(object : Callback<ApiResponse<MutableList<Product>>> {
                override fun onResponse(
                    call: Call<ApiResponse<MutableList<Product>>>,
                    response: Response<ApiResponse<MutableList<Product>>>
                ) {
                    if(response.isSuccessful){
                        val res: ApiResponse<MutableList<Product>> = response.body()!!

                        val listProduct = res.data as List<Product>
                        listProduct.forEach {
                            Log.d("Name: ", it.name!!)
                        }
                        Log.d("Message", "Successfully" )
                    }
                    else{
                        val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!);
                        Log.d("Error From Api", errorRes.message )
                    }
                    progressBar.visibility = ProgressBar.GONE
                }
                override fun onFailure(call: Call<ApiResponse<MutableList<Product>>>, t: Throwable) {
                    Log.d("Error", "Network" )
                    progressBar.visibility = ProgressBar.GONE
                }
            })
        }

        // Inflate the layout for this fragment
        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}