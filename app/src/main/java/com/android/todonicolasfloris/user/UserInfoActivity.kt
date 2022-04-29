package com.android.todonicolasfloris.user

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.lifecycle.lifecycleScope
import coil.imageLoader
import coil.load
import com.android.todonicolasfloris.R
import com.android.todonicolasfloris.network.Api
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.jar.Manifest

class UserInfoActivity : AppCompatActivity() {
    private val getPhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        val imageView = findViewById<ImageView>(R.id.image_view)
        imageView.load(bitmap)
        lifecycleScope.launch {
            if(bitmap != null) {
                val userInfo = Api.userWebService.updateAvatar(bitmap.toRequestBody()).body()
                imageView.load(userInfo?.avatar)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val photoTake = findViewById<Button>(R.id.take_picture_button)
        photoTake.setOnClickListener { launchCameraWithPermission()}

        }
    private val requestCamera =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { accepted ->
            if (accepted) getPhoto.launch()

            else showMessage(message = "Désolé vous n'avez pas le droit")
        }
    private fun showMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
            .setAction("Open Settings") {
                val intent = Intent(
                    ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                startActivity(intent)
            }
            .show()
    }
    private fun launchCameraWithPermission() {
        val camPermission = android.Manifest.permission.CAMERA
        val permissionStatus = checkSelfPermission(camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        when {
            isAlreadyAccepted -> getPhoto.launch()// lancer l'action souhaitée
                isExplanationNeeded -> showMessage(message = "t'as le seum")// afficher une explication
            else -> requestCamera.launch(camPermission)// lancer la demande de permission
        }
    }


    private fun Bitmap.toRequestBody(): MultipartBody.Part {
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            this.compress(Bitmap.CompressFormat.JPEG, 100, it) // this est le bitmap dans ce contexte
        }
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = tmpFile.readBytes().toRequestBody()
        )

    }
    }
