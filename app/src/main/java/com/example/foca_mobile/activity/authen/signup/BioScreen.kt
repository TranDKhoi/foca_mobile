package com.example.foca_mobile.activity.authen.signup

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.authen.login.LoginScreen
import com.example.foca_mobile.databinding.ActivityBioScreenBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.User
import com.example.foca_mobile.service.AuthService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BioScreen : AppCompatActivity() {

    private lateinit var binding: ActivityBioScreenBinding
    private lateinit var UserName: String
    private lateinit var Password: String
    private lateinit var imagePath: Uri
    var uri = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBioScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UserName = intent.getStringExtra("id").toString()
        Password = intent.getStringExtra("pass").toString()

        binding.finishbtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                confirmRegister()
            }

        })
    }

    public fun toLoginScreen(view: View) {

        val it: Intent = Intent(this, LoginScreen::class.java)
        startActivity(it)
        this.finish()
    }

    public fun confirmRegister() {

        val fname: EditText = binding.firstname
        val lname: EditText = binding.lastname
        val phone: EditText = binding.phonenum

        //IS VALID DATA
        if (fname.text.toString().isEmpty()) {
            fname.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            fname.setBackgroundResource(R.drawable.rounded_edittext_normal)

        if (lname.text.toString().isEmpty()) {
            lname.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            lname.setBackgroundResource(R.drawable.rounded_edittext_normal)

        if (phone.text.toString().isEmpty()) {
            phone.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            phone.setBackgroundResource(R.drawable.rounded_edittext_normal)

        if (fname.text.toString().isEmpty() ||
            lname.text.toString().isEmpty() ||
            phone.text.toString().isEmpty()
        )
            return;


        //UPLOAD IMAGE TO FIREBASE
        val userPhoto = GlobalScope.launch { val p = uploadToFirebase() }
        runBlocking { userPhoto.join() }

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("username", UserName)
        jsonObject.put("password", Password)
        jsonObject.put("confirmPassword", Password)
        jsonObject.put("firstName", fname.text.toString())
        jsonObject.put("lastName", lname.text.toString())
        jsonObject.put("phoneNumber", phone.text.toString())
        jsonObject.put("photoUrl", Uri.parse(uri))
        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        //CALL API
        val bioCall =
            ServiceGenerator.buildService(AuthService::class.java).registerUser(requestBody);

        binding.bar.visibility = ProgressBar.VISIBLE
        bioCall?.enqueue(object : Callback<ApiResponse<User>> {
            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
            ) {
                if (response.isSuccessful) {
                    binding.bar.visibility = ProgressBar.GONE
                    val responseBody = response.body()
                    val currentUser = responseBody!!.data;
                    toSuccessScreen(currentUser)
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!);
                    Log.d("Error From Api", errorRes.message)
                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_LONG)
                        .show()
                }
                binding.bar.visibility = ProgressBar.GONE
            }

            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
            }
        })

    }

    private fun toSuccessScreen(user: User) {
        val it = Intent(this, SuccessCreateAccountScreen::class.java);
        it.putExtra("currentUser", user);
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
            imagePath = data?.data!!
            circleavatar.setImageURI(imagePath);
        }
    }


    //FIREBASE IMAGE AREA
    private suspend fun uploadToFirebase() {

        var imageRef: StorageReference =
            FirebaseStorage.getInstance().reference.child("profile_images/$UserName.png")

        imageRef.putFile(imagePath)
            .addOnSuccessListener {
                uri =
                    "https://firebasestorage.googleapis.com/v0/b/foca-mobile.appspot.com/o/profile_images%2F$UserName.png"
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload Image", Toast.LENGTH_LONG).show()
            }
    }
}