package com.sn.harbinger.util.uicomponents

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textview.MaterialTextView
import com.sn.harbinger.R

/**
 * PageHeader,
 *
 * Params can be found at `attrs.xml` file
 */

class PageHeader @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var titleTextView: MaterialTextView

    private var titleText: String
        get() = titleTextView.text.toString()
        set(value) {
            titleTextView.text = value
        }

    private var titleTextColor: Int
        get() = titleTextView.currentTextColor
        set(value) {
            titleTextView.setTextColor(value)
        }

    init {

        inflate(context, R.layout.page_header, this)
        titleTextView = findViewById(R.id.tv_title)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.PageHeader)
        titleText = attributes.getString(R.styleable.PageHeader_titleText) ?: "Page header"

        titleTextColor = attributes.getColor(
            R.styleable.PageHeader_textColor,
            context.getColor(R.color.third_text_color)
        )
        titleTextView.setTextColor(titleTextColor)

        //maybe it can be made customizable.
        titleTextView.typeface = ResourcesCompat.getFont(context, R.font.dosis_medium)

        attributes.recycle()

    }

}