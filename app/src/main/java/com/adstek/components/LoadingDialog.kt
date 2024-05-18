package com.adstek.components

import android.app.Activity
import android.app.Dialog
import android.view.View
import com.adstek.R

class LoadingDialog() {

    private lateinit var dialog: Dialog

    constructor(activity: Activity) : this() {

        val dialogView = View.inflate(activity, R.layout.dialog_loading, null)
        dialog = Dialog(activity, R.style.loadingDialog)
        dialog.setContentView(dialogView)
        dialog.setCancelable(false)
    }

    private fun show() {

        if (!dialog.isShowing) {
            try {
                System.gc()

                dialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun dismiss() {
        System.gc()
        if (dialog.isShowing) dialog.dismiss()
    }

    fun setLoading(isLoading: Boolean) {
        if (isLoading)
            show()
        else
            dismiss()
    }

}