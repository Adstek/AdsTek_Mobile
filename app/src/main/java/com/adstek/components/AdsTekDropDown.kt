package com.adstek.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.adstek.R
import com.google.android.material.textfield.TextInputLayout

class AdsTekDropDown @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val textInputLayout: TextInputLayout
    private val textTitle: TextView
    private val autoCompleteTextView: AutoCompleteTextView

    init {
        LayoutInflater.from(context).inflate(R.layout.dropdown, this, true)

        textInputLayout = findViewById(R.id.dropdown_layout)
        autoCompleteTextView = findViewById(R.id.dropdown_autocomplete)
        textTitle = findViewById(R.id.dropdown_txTitle)

        context.obtainStyledAttributes(attrs, R.styleable.AdsText).apply {
            try {
                val placeholderText = getString(R.styleable.AdsText_hintText)
                val title = getString(R.styleable.AdsText_titleTextInput)
                textTitle.text = title
                placeholderText?.let { autoCompleteTextView.hint = it }

            } finally {
                recycle()
            }
        }
    }

    fun setDropdownList(items: Array<String>) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, items)
        autoCompleteTextView.setAdapter(adapter)
    }

    fun getSelectedValue(): String? {
        return autoCompleteTextView.text?.toString()
    }

    fun setSelectedValue(value: String) {
        autoCompleteTextView.setText(value, false)
    }

    fun getDropDownAutoText(): AutoCompleteTextView {
      return  autoCompleteTextView
    }
}