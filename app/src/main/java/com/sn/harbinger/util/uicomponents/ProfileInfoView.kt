package com.sn.harbinger.util.uicomponents

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textview.MaterialTextView
import com.sn.harbinger.R
import io.armcha.elasticview.ElasticView

class ProfileInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    var leftIcon: Drawable? = null
    var rightIcon: Drawable? = null
    var onClick: ((View) -> Unit)? = null

    @ColorInt
    var tintColor: Int = Color.parseColor("#FF136EB3")

    private var titleTextView: MaterialTextView

    private var titleText: String
        get() = titleTextView.text.toString()
        set(value) {
            titleTextView.text = value
        }

    init {
        inflate(context, R.layout.profile_info_view, this)
        val container = findViewById<ElasticView>(R.id.ev_change_pass)
        titleTextView = findViewById(R.id.title)

        container.setOnClickListener { view ->
            onClick?.invoke(view)
        }

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ProfileInfoView)
        titleText = attributes.getString(R.styleable.ProfileInfoView_title) ?: "Title"
        leftIcon = attributes.getDrawable(R.styleable.ProfileInfoView_leftIcon)
        rightIcon = attributes.getDrawable(R.styleable.ProfileInfoView_rightIcon)
        tintColor = attributes.getColor(R.styleable.ProfileInfoView_tintColor, this.tintColor)

        titleTextView.setCompoundDrawablesWithIntrinsicBounds(leftIcon, null, rightIcon, null)
        titleTextView.compoundDrawables[0].setTint(tintColor)

        attributes.recycle()
    }

}