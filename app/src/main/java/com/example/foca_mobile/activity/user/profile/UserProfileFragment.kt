package com.example.foca_mobile.activity.user.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
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
import com.bumptech.glide.Glide
import com.example.foca_mobile.activity.user.profile.generalsetting.GeneralSetting
import com.example.foca_mobile.utils.GlobalObject

@Suppress("DEPRECATION")
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
        binding.generalSettings.setOnClickListener {
            val intent = Intent(context, GeneralSetting::class.java)
            startActivityForResult(intent, 1)
        }
        binding.profileSettings.setOnClickListener {
            val intent = Intent(context, UserProfileActivity::class.java)
            startActivity(intent)
        }
        binding.logout.setOnClickListener {
            LoginPrefs.removeToken()
            activity?.finishAffinity()
            GlobalObject.currentSelectedPage = R.id.home
            val intent = Intent(context, LoginScreen::class.java)
            startActivity(intent)
        }
        Glide.with(requireContext())
            .load(GlobalObject.CurrentUser.photoUrl)
            .into(binding.userImage)
        binding.userName.text = GlobalObject.CurrentUser.fullName

        if (GlobalObject.CurrentUser.role == "USER")
            binding.userRole.text = resources.getString(R.string.Student)
        else
            binding.userRole.text = "Admin"
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = null
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("ObsoleteSdkInt")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fragmentManager?.beginTransaction()?.detach(this)?.commitNow()
            fragmentManager?.beginTransaction()?.attach(this)?.commitNow()
        } else {
            fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
        }
    }
}