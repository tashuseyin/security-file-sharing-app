package com.example.encryptedimagesharingapp.util

import android.Manifest
import android.content.Context
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

object Util {

    fun dispatchTakePictureGalleryIntent(
        context: Context,
        permissionListener: (PermissionType) -> Unit
    ) {
        Dexter.withContext(context)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    permissionListener(PermissionType.GRANTED)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(
                        context,
                        "You have denied storage permission.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(context, "Error occurred!", Toast.LENGTH_SHORT).show()
            }.onSameThread()
            .check()
    }

    enum class PermissionType {
        GRANTED
    }

}