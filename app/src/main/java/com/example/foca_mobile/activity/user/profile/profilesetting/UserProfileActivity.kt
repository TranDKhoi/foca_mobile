package com.example.foca_mobile.activity.user.profile.profilesetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityUserProfileBinding
import com.bumptech.glide.Glide
import com.example.foca_mobile.utils.GlobalObject

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var newArrayFavoriteFoodList: ArrayList<Favorite>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolBar()
        binding.toolbar.setNavigationOnClickListener {
            this.finish()
        }
        creatReviewRecycleview()
        binding.favoriteRecycleview.adapter = FavoriteAdapter(newArrayFavoriteFoodList)

        Glide.with(applicationContext)
            .load(GlobalObject.CurrentUser.photoUrl)
            .into(binding.userImage)
        binding.nameProfile.text = GlobalObject.CurrentUser.fullName
        binding.emailProfile.text = GlobalObject.CurrentUser.email
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }
    }

    private fun creatReviewRecycleview() {
        binding.favoriteRecycleview.layoutManager = LinearLayoutManager(this)
        binding.favoriteRecycleview.setHasFixedSize(true)
        newArrayFavoriteFoodList = arrayListOf<Favorite>()
        for (i in 0..5) {
            newArrayFavoriteFoodList.add(
                Favorite(
                    R.drawable.favorite_1,
                    "Spacy fresh crab",
                    "Waroenk kita",
                    35
                )
            )
            newArrayFavoriteFoodList.add(
                Favorite(
                    R.drawable.favorite_1,
                    "Spacy fresh crab",
                    "Waroenk kita",
                    35
                )
            )
        }
    }
}