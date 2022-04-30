package com.example.foca_mobile.activity.user.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.profile.settings.UserProfileActivity
import com.example.foca_mobile.databinding.FragmentUserProfileScreenBinding

class UserProfileScreenFragment : Fragment(R.layout.fragment_user_profile_screen) {

    private var _binding: FragmentUserProfileScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileScreenBinding.inflate(
            inflater,
            container,
            false
        )
        binding.profileSettings.setOnClickListener {
            val intent = Intent(context, UserProfileActivity::class.java)
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