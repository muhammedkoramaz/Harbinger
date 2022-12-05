package com.sn.harbinger.util.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import render.animations.Bounce
import render.animations.Render

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.inAnimation(context: Context, duration: Long = 1000) {
    val render = Render(context)
    render.setDuration(duration)
    render.setAnimation(Bounce().In(this))
    render.start()
}

fun View.inDownAnimation(context: Context, duration: Long = 1000) {
    val render = Render(context)
    render.setDuration(duration)
    render.setAnimation(Bounce().InDown(this))
    render.start()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}