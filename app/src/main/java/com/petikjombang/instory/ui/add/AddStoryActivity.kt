package com.petikjombang.instory.ui.add

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.petikjombang.instory.R
import com.petikjombang.instory.data.local.preferences.UserPreference
import com.petikjombang.instory.databinding.ActivityAddStoryBinding
import com.petikjombang.instory.services.createCustomTempFile
import com.petikjombang.instory.services.rotateBitmap
import com.petikjombang.instory.services.uriToFile
import com.petikjombang.instory.ui.ViewModelFactory
import com.petikjombang.instory.ui.list.MainActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AddStoryActivity : AppCompatActivity() {

    private lateinit var addStoryBinding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private val addStoryViewModels: AddStoryViewModels by viewModels {
        ViewModelFactory(this)
    }
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addStoryBinding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(addStoryBinding.root)

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSION,
                REQUEST_CODE_PERMISSIONS
            )
        }

        addStoryBinding.btnKameraAddStory.setOnClickListener { openCamera() }
        addStoryBinding.btnGaleriAddStory.setOnClickListener { openGalery() }
        addStoryBinding.btnAddStory.setOnClickListener { addStory() }
    }

    private fun reduceImage(file: File) : File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun addStory() {
        if (getFile != null) {
            val file = reduceImage(getFile as File)
            val description = addStoryBinding.etDescAddStory.text.toString()
            val desc = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val request = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                request
            )
            val progressBar = addStoryBinding.progsbarAddStory
            addStoryViewModels.addStory(imageMultipart, desc).observe(this){ result ->
                if (result != null) {
                    when (result) {
                        is com.petikjombang.instory.data.Result.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }

                        is com.petikjombang.instory.data.Result.Success -> {
                            progressBar.visibility = View.GONE
                            val message = result.data.message
                            Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }

                        is com.petikjombang.instory.data.Result.Error -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Gagal Menambahkan Story", Toast.LENGTH_SHORT).show()

                        }
                    }
                }

            }

        } else {
            Toast.makeText(this, "Lengkapi Seluruh Data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGalery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")

        launcherIntentGallery.launch(chooser)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.petikjombang.instory",
                it
            )

            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                true
            )

            addStoryBinding.imgAddStory.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)

            getFile = myFile
            addStoryBinding.imgAddStory.setImageURI(selectedImg)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSION.all  {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSION = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}