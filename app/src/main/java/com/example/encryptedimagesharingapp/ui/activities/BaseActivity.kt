package com.example.encryptedimagesharingapp.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.encryptedimagesharingapp.R
import com.google.android.material.snackbar.Snackbar


open class BaseActivity : AppCompatActivity() {

    private var dialog: Dialog? = null

    fun showDialog() {
        dialog = Dialog(this)
        dialog?.let {
            it.apply {
                setContentView(R.layout.dialog)
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                show()
            }
        }
    }

    fun hideDialog() {
        dialog?.dismiss()
    }

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.red
                )
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.green
                )
            )
        }
        snackBar.show()
    }
}