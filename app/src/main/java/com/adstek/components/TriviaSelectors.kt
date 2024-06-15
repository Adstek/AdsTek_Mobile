package com.adstek.components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.adstek.R

class TriviaSelectors @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var cardText: TextView
     var answersSelectorLayout: ConstraintLayout

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.trivia_selector, this, true)
        cardText = view.findViewById(R.id.possibleAnswerd)
        answersSelectorLayout = view.findViewById(R.id.answersSelectorLayout)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TriviaSelector, 0, 0)
            val text = typedArray.getString(R.styleable.TriviaSelector_answers)

            setText(text ?: "Default Text")
            typedArray.recycle()
        }
    }

    fun setText(text: String) {
        cardText.text = text
    }

    fun getText(): String {
       return cardText.text.toString()
    }


    fun setbackgroundcolor(backgroundColor: String, textColor: String) {
        answersSelectorLayout.setBackgroundColor( Color.parseColor(backgroundColor))
        cardText.setTextColor(Color.parseColor(textColor))
    }
}
