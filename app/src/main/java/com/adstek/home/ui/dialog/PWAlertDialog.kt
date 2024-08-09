package com.adstek.home.ui.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.adstek.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PWAlertDialog  constructor(
   context: Context,
    title: String? = "",
   message: String? = ""
) : MaterialAlertDialogBuilder(context) {

    init {
        val background = ContextCompat.getDrawable(context, R.drawable.fragment_dialog_layout_background)
        setBackground(background)
        title?.let { setTitle(it) }
        message?.let { setMessage(it) }
    }

    override fun create(): AlertDialog {
        return super.create()
    }

    override fun setTitle(titleId: Int): MaterialAlertDialogBuilder {
        return super.setTitle(titleId)
    }

    override fun setMessage(messageId: Int): MaterialAlertDialogBuilder {
        return super.setMessage(messageId)
    }

    override fun setNegativeButton(textId: Int, listener: DialogInterface.OnClickListener?): MaterialAlertDialogBuilder {
        return super.setNegativeButton(textId, listener)
    }

    override fun setPositiveButton(textId: Int, listener: DialogInterface.OnClickListener?): MaterialAlertDialogBuilder {
        return super.setPositiveButton(textId, listener)
    }

    override fun setBackground(background: Drawable?): MaterialAlertDialogBuilder {
        return super.setBackground(background)
    }

    override fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?): MaterialAlertDialogBuilder {
        return super.setOnDismissListener(onDismissListener)
    }
}