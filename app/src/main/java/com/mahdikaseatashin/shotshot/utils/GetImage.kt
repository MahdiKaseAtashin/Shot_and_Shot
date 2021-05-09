package com.mahdikaseatashin.shotshot.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.mahdikaseatashin.shotshot.view.add.AddEditUserActivity
import java.io.ByteArrayOutputStream

object GetImage {
    fun captureImage(activityEdit: AddEditUserActivity) {
        Dexter.withContext(activityEdit)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    activityEdit.startActivityForResult(cameraIntent, AddEditUserActivity.CAPTURE_PHOTO_CODE)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    if (p0?.isPermanentlyDenied == true) {
                        AlertDialog.Builder(activityEdit)
                            .setTitle("Permission Denied")
                            .setMessage("We need you to allow us to access camera for this feature\nfor this thing to happen go to setting and give us the permission")
                            .setPositiveButton("Setting") { _, _ ->
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri: Uri = Uri.fromParts("package", activityEdit.packageName, null)
                                intent.data = uri
                                activityEdit.startActivity(intent)
                            }
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).check()
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun chooseImageFromGallery(activityEdit: AddEditUserActivity) {
        Dexter.withContext(activityEdit)
            .withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ).withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val pickPhoto = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    activityEdit.startActivityForResult(pickPhoto, AddEditUserActivity.PICK_PHOTO_CODE)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    if (p0?.isPermanentlyDenied == true) {
                        AlertDialog.Builder(activityEdit)
                            .setTitle("Permission Denied")
                            .setMessage("We need you to allow us to read gallery for this feature\nfor this thing to happen go to setting and give us the permission")
                            .setPositiveButton("Setting") { _, _ ->
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri: Uri = Uri.fromParts("package", activityEdit.packageName, null)
                                intent.data = uri
                                activityEdit.startActivity(intent)
                            }
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            }).check()

    }
}
