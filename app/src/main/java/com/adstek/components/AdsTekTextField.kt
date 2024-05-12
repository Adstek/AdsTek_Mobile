package com.adstek.components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.adstek.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AdsTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val textInputLayout: TextInputLayout
    private val textInputEditText: TextInputEditText
    private val titleTextField: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.text_field_layout, this, true)
        textInputLayout = findViewById(R.id.textInputLayout)
        textInputEditText = findViewById(R.id.textInputEditText)
        titleTextField = findViewById(R.id.textFieldTitle)

        // Apply custom attributes
        context.obtainStyledAttributes(attrs, R.styleable.AdsTextField).apply {
            try {
                val placeholderText = getString(R.styleable.AdsTextField_hintText)
                val titleTextFieldInput = getString(R.styleable.AdsTextField_titleTextInput)
                val startIconDrawable = getResourceId(R.styleable.AdsTextField_leadingIcon, 0)

                // Set hint
                textInputLayout.placeholderText = placeholderText
                titleTextField.text = titleTextFieldInput


                // Add focus change listener to TextInputEditText
                textInputEditText.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        textInputLayout.boxBackgroundColor = Color.parseColor("#FFFFFF")
                    } else {
                        textInputLayout.boxBackgroundColor = Color.parseColor("#EFF1F3")
                    }
                }


                // Set start icon
                if (startIconDrawable != 0) {
                    textInputLayout.startIconDrawable = ContextCompat.getDrawable(context, startIconDrawable)
                }
            } finally {
                recycle()
            }
        }
    }

    // Expose TextInputLayout and TextInputEditText as public methods
    fun getTextInputLayout(): TextInputLayout {
        return textInputLayout
    }

    fun getTextInputEditText(): TextInputEditText {
        return textInputEditText
    }
}