package com.example.encryptedimagesharingapp.util

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.*


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

    fun convertImageToByteArray(context: Context, uri: Uri): ByteArray {
        var data: ByteArray? = null
        try {
            val cr: ContentResolver = context.contentResolver
            val inputStream: InputStream? = cr.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            data = baos.toByteArray()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return data!!
    }

    fun decryptFile(context: Context, file: File, uid: String?){
        val newData = ByteArray(file.readBytes().size)
        val fileName = file.name.split(".")[0]
        val fileExt = file.name.split(".")[1]
        var index = 0
        var finalNumber = 0

        uid?.forEach {
            finalNumber = (it.toInt() + finalNumber) % 100
        }

        file.readBytes().forEach {
            newData.set(index, (it + finalNumber).toByte())
            index++
        }
        Toast.makeText(context, "Decrypt successful", Toast.LENGTH_SHORT).show()
        saveFile(newData, fileName, fileExt, file)
    }

    fun saveFile(bytes: ByteArray, fileName: String, fileExt: String, oldFile: File) {
        val path = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath)
        var file = File.createTempFile("decrypted" + fileName,".$fileExt", path)
        var os = FileOutputStream(file)
        os.write(bytes)
        os.close()
        oldFile.delete()
    }
}