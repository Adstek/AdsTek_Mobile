package com.adstek.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.adstek.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AdsTekButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val button: MaterialButton

    init {
        LayoutInflater.from(context).inflate(R.layout.button_layout, this, true)
        button = findViewById(R.id.btn)
        // Apply custom attributes
        context.obtainStyledAttributes(attrs, R.styleable.AdsText).apply {
            try {
                val buttonText = getString(R.styleable.AdsText_buttonText)

                // Set hint
                button.text = buttonText

            } finally {
                recycle()
            }
        }
    }
    fun onClick(listener: OnClickListener) {
        button.setOnClickListener(listener)
    }
}