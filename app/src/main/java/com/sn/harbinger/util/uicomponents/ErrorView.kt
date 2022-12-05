package com.sn.harbinger.util.uicomponents

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.textview.MaterialTextView
import com.sn.harbinger.R

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    var onClick: ((View) -> Unit)? = null
    private var tvErrText: MaterialTextView
    var tvTryAgain: MaterialTextView

    var errText: String
        get() = tvErrText.text.toString()
        set(value) {
            tvErrText.text = value
        }

    init {

        inflate(context, R.layout.error_view, this)
        tvErrText = findViewById(R.id.tv_err)
        tvTryAgain = findViewById(R.id.tv_try_again)

        tvTryAgain.setOnClickListener {
            onClick?.invoke(it)
        }

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ErrorView)
        errText = attributes.getString(R.styleable.ErrorView_errText)
            ?: context.getString(R.string.unexpected_error)

        attributes.recycle()
    }


}