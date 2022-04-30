package com.example.foca_mobile.activity.user.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.FragmentUserProfileScreenBinding

class UserProfileScreenFragment : Fragment(R.layout.fragment_user_profile_screen) {

    private var _binding: FragmentUserProfileScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileScreenBinding.inflate(
            inflater,
            container,
            false
        )
        binding.generalSettings.setOnClickListener {}
        binding.profileSettings.setOnClickListener {
            Log.d("abc","ok")
            val intent = Intent(context, UserProfile_Activity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = null
    }
}