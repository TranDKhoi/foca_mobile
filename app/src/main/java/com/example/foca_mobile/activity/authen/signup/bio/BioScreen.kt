package com.example.foca_mobile.activity.authen.signup.bio

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.activity.authen.signup.success.SuccessScreen


class BioScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bio_screen)
    }

    public fun toLoginScreen(view: View) {

        val it: Intent = Intent(this, LoginScreen::class.java);
        startActivity(it);
        this.finish();
    }

    public fun toSuccessScreen(view : View){
        val it : Intent = Intent(this, SuccessScreen::class.java);
        finishAffinity();
        startActivity(it);
        this.finish();
    }


    // IMAGE LOGIC AREA
    private fun chooseImage() {
        val it: Intent = Intent(Intent.ACTION_PICK);
        it.type = "image/*";
        startActivityForResult(it, IMAGE_PICK_CODE)
    }

    public fun checkPermission(view: View) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                chooseImage();
            }
        } else {
            chooseImage();
        }
    }

    companion object {
        private val IMAGE_PICK_CODE = 1000;
        private val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImage();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            val circleavatar: ImageButton = findViewById(R.id.circleavatar) as ImageButton;
            circleavatar.setImageURI(data?.data);
        }
    }
}