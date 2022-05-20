package com.example.foca_mobile.activity.admin.menu

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.example.foca_mobile.R
import com.example.foca_mobile.databinding.ActivityAdminCreateProductBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Product
import com.example.foca_mobile.service.ProductService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.utils.ErrorUtils
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdminCreateProduct : AppCompatActivity() {

    private lateinit var binding: ActivityAdminCreateProductBinding
    private lateinit var imagePath: Uri
    private var uri: Uri? = null
    private var selectedType = ""
    private var product: Product? = null
    private var tempName: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCreateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val objStr = intent.getStringExtra("product")
        if (objStr != "") {
            product = Gson().fromJson(objStr, Product::class.java)
            binding.headerTitle.text = resources.getString(R.string.UpdateProduct)
            binding.finishbtn.text = resources.getString(R.string.Update)
            binding.productName.setText(product!!.name)
            binding.productPrice.setText(product!!.price.toString())
            binding.productDes.setText(product!!.description)
            binding.productType.text = product!!.type
            selectedType = product!!.type.toString()
            tempName = product!!.id.toString()
            Glide.with(applicationContext)
                .load(product!!.image)
                .into(binding.productImage)
            binding.finishbtn.setOnClickListener { confirmUpdate() }
            imagePath = Uri.parse("")
            binding.btnDelete.visibility = AppCompatButton.VISIBLE
        } else {
            binding.headerTitle.text = resources.getString(R.string.CreateProduct)
            binding.finishbtn.text = resources.getString(R.string.AddNew)
            binding.finishbtn.setOnClickListener { confirmCreate() }
            binding.btnDelete.visibility = AppCompatButton.GONE
        }


        binding.imageCard.setOnClickListener {
            checkPermission()
        }
        binding.backBtn.setOnClickListener { this.finish() }
        binding.btnDelete.setOnClickListener { buildDeleteAlert() }

        //initTypeSpinner
        initSpinner()

    }

    private fun buildDeleteAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.Warning))
        builder.setMessage(resources.getString(R.string.Areyousure))
        builder.setPositiveButton(resources.getString(R.string.YES)) { dialog, _ ->
            confirmDelete()
            dialog.dismiss()
        }
        builder.setNegativeButton(
            resources.getString(R.string.NO)
        ) { dialog, which -> // Do nothing
            dialog.dismiss()
        }
        val alert = builder.create()


        alert.setOnShowListener {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        }

        alert.show()
    }

    private fun confirmUpdate() {
        if (isValidData(true)) {
            binding.bar.visibility = ProgressBar.VISIBLE

            GlobalScope.launch(Dispatchers.IO) {
                if (imagePath.toString() != "")
                    uri = uploadToFirebase()
                withContext(Dispatchers.Main) {
                    // Create JSON using JSONObject
                    val jsonObject = JSONObject()
                    jsonObject.put("name", binding.productName.text.trim())
                    jsonObject.put("price", binding.productPrice.text.trim())
                    jsonObject.put("description", binding.productDes.text.trim())
                    jsonObject.put("type", selectedType)
                    if (uri != null)
                        jsonObject.put("image", uri)
                    Log.d("Import image success", uri.toString())

                    // Convert JSONObject to String
                    val jsonObjectString = jsonObject.toString()

                    val requestBody =
                        jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

                    //CALL API
                    val updateCall =
                        ServiceGenerator.buildService(ProductService::class.java)
                            .updateProduct(product!!.id.toString(), requestBody)

                    updateCall?.enqueue(object : Callback<ApiResponse<Product>> {
                        override fun onResponse(
                            call: Call<ApiResponse<Product>>,
                            response: Response<ApiResponse<Product>>
                        ) {
                        }

                        override fun onFailure(call: Call<ApiResponse<Product>>, t: Throwable) {
                        }
                    })
                    Toast.makeText(
                        applicationContext,
                        "Update product successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.bar.visibility = ProgressBar.GONE
                }
            }

        }
    }

    private fun confirmCreate() {

        if (isValidData(false)) {
            binding.bar.visibility = ProgressBar.VISIBLE

            GlobalScope.launch(Dispatchers.IO) {
                uri = uploadToFirebase()
                withContext(Dispatchers.Main) {
                    // Create JSON using JSONObject
                    val jsonObject = JSONObject()
                    jsonObject.put("name", binding.productName.text.trim())
                    jsonObject.put("price", binding.productPrice.text.trim())
                    jsonObject.put("description", binding.productDes.text.trim())
                    jsonObject.put("type", selectedType)
                    if (uri != null)
                        jsonObject.put("image", uri)
                    Log.d("Import image success", uri.toString())

                    // Convert JSONObject to String
                    val jsonObjectString = jsonObject.toString()

                    val requestBody =
                        jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

                    //CALL API
                    val createCall =
                        ServiceGenerator.buildService(ProductService::class.java)
                            .createProduct(requestBody)

                    binding.bar.visibility = ProgressBar.VISIBLE
                    createCall?.enqueue(object : Callback<ApiResponse<Product>> {
                        override fun onResponse(
                            call: Call<ApiResponse<Product>>,
                            response: Response<ApiResponse<Product>>
                        ) {
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                val product = responseBody!!.data
                                tempName = product.id.toString()
                                runOnUiThread {
                                    Toast.makeText(
                                        applicationContext,
                                        "Create product successfully!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                runBlocking { uploadToFirebase() }
                            } else {
                                val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!)
                                Log.d("Error From Api", errorRes.message)
                                Toast.makeText(
                                    applicationContext,
                                    response.message(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse<Product>>, t: Throwable) {
                        }
                    })
                    binding.bar.visibility = ProgressBar.GONE
                    Thread.sleep(2000)
                    this@AdminCreateProduct.finish()
                }
            }
        }
    }

    private fun isValidData(isUpdate: Boolean): Boolean {
        //IS VALID DATA
        if (binding.productName.text.isEmpty()) {
            binding.productName.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            binding.productName.setBackgroundResource(R.drawable.rounded_edittext_normal)

        if (binding.productPrice.text.isEmpty()) {
            binding.productPrice.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            binding.productPrice.setBackgroundResource(R.drawable.rounded_edittext_normal)

        if (binding.productDes.text.isEmpty()) {
            binding.productDes.setBackgroundResource(R.drawable.rounded_edittext_error)
        } else
            binding.productDes.setBackgroundResource(R.drawable.rounded_edittext_normal)
        if (!isUpdate)
            if (imagePath.toString().isEmpty())
                Toast.makeText(this, "Image can not be null", Toast.LENGTH_SHORT).show()
        if (!isUpdate) {
            if (binding.productName.text.isEmpty() ||
                binding.productPrice.text.isEmpty() ||
                binding.productDes.text.isEmpty() ||
                imagePath.toString().isEmpty()
            )
                return false
        } else {
            if (binding.productName.text.isEmpty() ||
                binding.productPrice.text.isEmpty() ||
                binding.productDes.text.isEmpty()
            )
                return false
        }
        return true
    }

    private fun initSpinner() {

        binding.productType.setOnClickListener {
            // setup the alert builder
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setTitle(resources.getString(R.string.Filtertheorders))

            // add a list
            val status = arrayOf(
                resources.getString(R.string.FOOD),
                resources.getString(R.string.DRINK)
            )
            builder.setItems(status) { _, which ->
                when (which) {
                    0 -> {
                        selectedType = "FOOD"
                        binding.productType.text = resources.getString(R.string.FOOD)
                    }
                    1 -> {
                        selectedType = "DRINK"
                        binding.productType.text = resources.getString(R.string.DRINK)
                    }
                }
            }
            // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun confirmDelete() {
        //CALL API
        var deleteCall = ServiceGenerator.buildService(ProductService::class.java)
            .softDeleteProduct(product!!.id.toString())

        deleteCall?.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    this@AdminCreateProduct.finish()
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!);
                    Log.d("Error From Api", errorRes.message)
                    Toast.makeText(applicationContext, errorRes.message, Toast.LENGTH_LONG).show();
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
            }
        })
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

            imagePath = data?.data!!
            binding.productImage.scaleType = ImageView.ScaleType.FIT_XY
            binding.productImage.setImageURI(imagePath)
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }


    //FIREBASE IMAGE AREA
    private suspend fun uploadToFirebase(): Uri {
        val imageRef: StorageReference =
            FirebaseStorage.getInstance().reference.child("product_images/$tempName.png")

        return withContext(Dispatchers.IO) {
            imageRef.putFile(imagePath)
                .await()
                .storage
                .downloadUrl
                .await() // await the url
        }
    }
}