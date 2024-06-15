package com.adstek.extensions

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import com.adstek.data.remote.response.Event
import com.adstek.components.LoadingDialog
import com.adstek.data.remote.response.DataState
import com.adstek.util.SharedPref
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber
import java.io.File

fun Fragment.selectImageAndReturnUri(
    imageCode: Int,
    cameraOnly: Boolean = false,
    galleryOnly: Boolean = false,
    onImageSelected: (Uri?) -> Unit
): ActivityResultLauncher<Intent> {

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == FragmentActivity.RESULT_OK) {
            val uri = result.data?.data
            onImageSelected(uri)
        } else {
            onImageSelected(null)
        }
    }

    val imagePickerIntent = ImagePicker.with(this)
        .cropSquare()
        .compress(1024)
        .maxResultSize(1080, 1080)
        .saveDir(File(this.requireActivity(). getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ImagePicker"))

    if (cameraOnly) {
        imagePickerIntent.cameraOnly()
    }
    if (galleryOnly) {
        imagePickerIntent.galleryOnly()
    }

    val intent = Intent(this.requireActivity(), ImagePicker::class.java)
    imagePickerIntent.createIntent { intent }
    imagePickerIntent.start(imageCode) // Start the image picker
    launcher.launch(intent)
    return launcher
}

fun Fragment.toast(msg: String?) {
    Toast.makeText(requireActivity(), "$msg", Toast.LENGTH_LONG).show()
}

fun Fragment.loadFromFile(context: Context, imageView: ImageView, filePath: String,) {
    Glide.with(context)
        .load(filePath)
        .into(imageView)
}

fun <T> Fragment.observeEventLiveData(
    liveData: LiveData<Event<DataState<T>>>,
    enableProgressBar: Boolean = true,
    onError: ((String) -> Unit)? = null,   // Change here: accepting a String parameter
    onSuccess: (T?) -> Unit
) {

    val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }

    liveData.observe(viewLifecycleOwner) { event ->
        event.getContentIfNotHandled()?.let { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    loadingDialog.setLoading(enableProgressBar)
                }
                is DataState.Success -> {
                    loadingDialog.setLoading(false)
                    onSuccess(dataState.data)
                }
                is DataState.Error -> {
                    loadingDialog.setLoading(false)
                    Timber.tag("ErrorResponse").d("${dataState.exception}")
                    dataState.exception.message?.let { errorMsg ->
                        onError?.invoke(errorMsg)  // Pass errorMsg to the onError lambda

                    }

                }
            }
        }
    }
}

fun <T> Fragment.observeLiveData(
    liveData: LiveData<DataState<T>>,
    enableProgressBar: Boolean = true,
    onError: ((String) -> Unit)? = null,   // Change here: accepting a String parameter
    onSuccess: (T?) -> Unit
) {

    val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }

    liveData.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    loadingDialog.setLoading(enableProgressBar)
                }
                is DataState.Success -> {
                    loadingDialog.setLoading(false)
                    onSuccess(dataState.data)
                }
                is DataState.Error -> {
                    loadingDialog.setLoading(false)
                    Timber.tag("ErrorResponse").d("${dataState.exception}")
                    dataState.exception.message?.let { errorMsg ->
                        onError?.invoke(errorMsg)  // Pass errorMsg to the onError lambda

                    }

                }
            }
        }
    }



fun Fragment.setCustomFocusChangeListener(
    view: View,
    getFieldText: () -> String?,
    key: String,
    layout: TextInputLayout,
    sharedPref: SharedPref
) {
    view.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            val fieldText = getFieldText()
            if (fieldText?.isNotEmpty() == true) {
                sharedPref.setPref(key, fieldText)
                layout.boxBackgroundColor = Color.parseColor("#EFF1F3")
            } else {
                layout.boxBackgroundColor = Color.parseColor("#FFFFFF")
            }
        } else {
            layout.boxBackgroundColor = Color.parseColor("#FFFFFF")
        }
    }
}

fun Fragment.setCustomFocusChangeListenerForDropdown(
    view: View,
    getSelectedValue: () -> String?,
    key: String,
    sharedPref: SharedPref
) {
    view.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            val selectedValue = getSelectedValue()
            if (!selectedValue.isNullOrEmpty()) {
                sharedPref.setPref(key, selectedValue)
            }
        }
    }
}