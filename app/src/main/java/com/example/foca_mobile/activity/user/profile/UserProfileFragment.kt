package com.example.foca_mobile.activity.user.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.adapterClass.ReviewFoodAdapter
import com.example.foca_mobile.databinding.FragmentUserProfileBinding
import com.example.foca_mobile.model.ReviewFood
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var newArrayReviewFoodList: ArrayList<ReviewFood>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(
            inflater,
            container,
            false
        )
        creatReviewRecycleview()
        binding.reviewRecycleview.adapter = ReviewFoodAdapter(newArrayReviewFoodList)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun creatReviewRecycleview() {
        binding.reviewRecycleview.layoutManager = LinearLayoutManager(activity)
        binding.reviewRecycleview.setHasFixedSize(true)
        newArrayReviewFoodList = arrayListOf<ReviewFood>()
        for (i in 0..5) {
            newArrayReviewFoodList.add(
                ReviewFood(
                    R.drawable.photo_profile_1,
                    "Dianne Russell",
                    LocalDate.parse("2022-04-18"),
                    "This Is great, So delicious! You Must Here, With Your family . . ",
                    5
                )
            )
            newArrayReviewFoodList.add(
                ReviewFood(
                    R.drawable.photo_profile_2,
                    "Dianne Russell",
                    LocalDate.parse("2022-04-18"),
                    "This Is great, So delicious! You Must Here, With Your family . . ",
                    5
                )
            )
        }
    }
}