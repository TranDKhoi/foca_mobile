package com.example.foca_mobile.activity.authen.signup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.example.foca_mobile.utils.GlobalObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BioScreen : AppCompatActivity() {

    private lateinit var binding: ActivityBioScreenBinding
    private lateinit var userName: String
    private lateinit var password: String
    private lateinit var imagePath: Uri
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBioScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("id").toString()
        password = intent.getStringExtra("pass").toString()

        binding.finishbtn.setOnClickListener {
            confirmRegister()
        }
        binding.backBtn.setOnClickListener { toLoginScreen() }
        binding.circleavatar.setOnClickListener { checkPermission() }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun toLoginScreen() {
        val it = Intent(this, LoginScreen::class.java)
        startActivity(it)
        this.finish()
    }

    private fun confirmRegister() {

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
            return

        GlobalScope.launch(Dispatchers.IO) {
            uri = uploadToFirebase()
            withContext(Dispatchers.Main) {
                // Create JSON using JSONObject
                val jsonObject = JSONObject()
                jsonObject.put("username", userName)
                jsonObject.put("password", password)
                jsonObject.put("confirmPassword", password)
                jsonObject.put("firstName", fname.text.toString())
                jsonObject.put("lastName", lname.text.toString())
                jsonObject.put("phoneNumber", phone.text.toString())
                if (uri != null)
                    jsonObject.put("photoUrl", uri)
                Log.d("Import image success", uri.toString())

                // Convert JSONObject to String
                val jsonObjectString = jsonObject.toString()

                val requestBody =
                    jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

                //CALL API
                val bioCall =
                    ServiceGenerator.buildService(AuthService::class.java).registerUser(requestBody)

                binding.bar.visibility = ProgressBar.VISIBLE
                bioCall?.enqueue(object : Callback<ApiResponse<User>> {
                    override fun onResponse(
                        call: Call<ApiResponse<User>>,
                        response: Response<ApiResponse<User>>
                    ) {
                        if (response.isSuccessful) {
                            binding.bar.visibility = ProgressBar.GONE
                            val responseBody = response.body()
                            val currentUser = responseBody!!.data
                            toSuccessScreen(currentUser)
                        } else {
                            val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                            Log.d("Error From Api", errorRes.message)
                            Toast.makeText(
                                applicationContext,
                                response.message(),
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                        binding.bar.visibility = ProgressBar.GONE
                    }

                    override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                    }
                })
            }
        }
    }

    private fun toSuccessScreen(user: User) {
        val it = Intent(this, SuccessCreateAccountScreen::class.java)
        GlobalObject.CurrentUser = user
        finishAffinity()
        startActivity(it)
        this.finish()
    }


    // IMAGE LOGIC AREA
    @Suppress("DEPRECATION")
    private fun chooseImage() {
        val it = Intent(Intent.ACTION_PICK)
        it.type = "image/*"
        startActivityForResult(it, IMAGE_PICK_CODE)
    }

    private fun checkPermission() {

        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_DENIED
        ) {
            val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions, PERMISSION_CODE)
        } else {
            chooseImage()
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImage()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            val circleavatar: ImageButton = findViewById(R.id.circleavatar)
            imagePath = data?.data!!
            circleavatar.setImageURI(imagePath)
        }
    }

    //FIREBASE IMAGE AREA
    private suspend fun uploadToFirebase(): Uri {
        val imageRef: StorageReference =
            FirebaseStorage.getInstance().reference.child("profile_images/$userName.png")

        return withContext(Dispatchers.IO) {
            imageRef.putFile(imagePath)
                .await()
                .storage
                .downloadUrl
                .await() // await the url
        }
    }
}