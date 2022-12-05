package com.sn.harbinger.util.extensions

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sn.harbinger.R

enum class SnackBarType { SUCCESS, ERROR }

fun Fragment.showSnackBar(msg: String, type: SnackBarType, view: View) {
    val snackBar = Snackbar.make(
        view, msg,
        Snackbar.LENGTH_LONG
    )
    val color: Int =
        if (type == SnackBarType.SUCCESS) R.color.success_color else R.color.error_color
    snackBar.setBackgroundTint(this.resources.getColor(color, null))
    snackBar.show()
}