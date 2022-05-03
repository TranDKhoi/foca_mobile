package com.example.foca_mobile.activity.user.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.activity.user.profile.profilesetting.UserProfileActivity
import com.example.foca_mobile.databinding.FragmentUserProfileBinding
import com.example.foca_mobile.utils.LoginPrefs

class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(
            inflater,
            container,
            false
        )
        binding.profileSettings.setOnClickListener {
            val intent = Intent(context, UserProfileActivity::class.java)
            startActivity(intent)
        }
        binding.logout.setOnClickListener {
            LoginPrefs.removeToken();
            val it = Intent(context, LoginScreen::class.java);
            activity?.finishAffinity()
            startActivity(it);
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = null
    }
}