package com.adstek.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.adstek.R

class AdsTekCardSelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var cardImage: ImageView
    private var cardText: TextView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.ads_tek_card_selector, this, true)
        cardImage = view.findViewById(R.id.cardImage)
        cardText = view.findViewById(R.id.cardText)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.AdsTekCardSelector, 0, 0)
            val text = typedArray.getString(R.styleable.AdsTekCardSelector_text)
            val imageResId = typedArray.getResourceId(R.styleable.AdsTekCardSelector_image, R.drawable.outline_person_24)

            setText(text ?: "Default Text")
            setImage(imageResId)

            typedArray.recycle()
        }
    }

    fun setText(text: String) {
        cardText.text = text
    }

    fun setImage(resId: Int) {
        cardImage.setImageResource(resId)
    }
}
