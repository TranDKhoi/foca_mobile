package com.example.foca_mobile.activity.user.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import com.example.foca_mobile.R
import com.example.foca_mobile.models.Post
import com.example.foca_mobile.retrofit.PostService
import com.example.foca_mobile.retrofit.ServiceGenerator
import kotlinx.android.synthetic.main.fragment_home.*
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
            var getPostCall = ServiceGenerator.buildService(PostService::class.java).getPostList();
            progressBar.visibility = ProgressBar.VISIBLE

            Log.d("Main Activity", "before call api")
            getPostCall.enqueue(object : Callback<MutableList<Post>> {
                override fun onResponse(
                    call: Call<MutableList<Post>>,
                    response: Response<MutableList<Post>>
                ) {
                    progressBar.visibility = ProgressBar.GONE
                }

                override fun onFailure(call: Call<MutableList<Post>>, t: Throwable) {
                    progressBar.visibility = ProgressBar.GONE
                }
            })
            Log.d("Main Activity", "after call api")
        }

        // Inflate the layout for this fragment
        return view

    }

    fun getAllPost() {
        var getPostCall = ServiceGenerator.buildService(PostService::class.java).getPostList();

        Log.d("Main Activity", "before call api")
        getPostCall.enqueue(object : Callback<MutableList<Post>> {
            override fun onResponse(
                call: Call<MutableList<Post>>,
                response: Response<MutableList<Post>>
            ) {
            }

            override fun onFailure(call: Call<MutableList<Post>>, t: Throwable) {
            }

        })
        Log.d("Main Activity", "after call api")
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