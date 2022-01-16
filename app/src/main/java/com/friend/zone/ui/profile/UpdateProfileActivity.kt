package com.friend.zone.ui.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.friend.zone.R
import com.friend.zone.databinding.ActivityUpdateProfileBinding
import com.nuryazid.core.api.ApiStatus
import com.nuryazid.core.base.activity.CoreActivity
import com.nuryazid.core.extension.compress
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UpdateProfileActivity : CoreActivity<ActivityUpdateProfileBinding, UpdateProfileViewModel>(R.layout.activity_update_profile) {

    private val REQUEST_IMAGE_CAPTURE = 1

    var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this

        viewModel.apiResponse.observe(this, { response ->
            when (response.status) {
                ApiStatus.LOADING -> loadingDialog.show(response.message)
                ApiStatus.ERROR -> loadingDialog.show(response.message, false)
                ApiStatus.SUCCESS -> loadingDialog.show(response.message, false).onDismiss { finish() }
            }
        })
    }

    fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { cameraIntent ->

            cameraIntent.resolveActivity(packageManager)?.also {

                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", it)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${createFileName()}_", /* prefix */".jpg", /* suffix */storageDir /* directory */).apply {
            // Save a file: path for use with ACTION_VIEW intents
            photoFile = File(absolutePath)
        }
    }

    private fun createFileName() = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

    private fun setPic() {
        // Get the dimensions of the View
        val targetW: Int = binding.ivPhoto.width
        val targetH: Int = binding.ivPhoto.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(photoFile?.absolutePath, bmOptions)?.also { bitmap ->

            photoFile?.absolutePath?.let { path ->
                val ei = ExifInterface(path)

                val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

                val rotatedBitmap = when(orientation) {

                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                    else -> bitmap
                }

                photoFile?.compress(this, 512, 512, 300) {
                    binding.ivPhoto.load(rotatedBitmap) {
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic()
        }
    }
}