package com.example.testapp.ui.utils

import android.app.Activity
import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import com.example.testapp.R
import com.google.android.material.snackbar.Snackbar

/**
 * Just a SnackBar extensions for UX
 *
 */

fun Activity.showSnackbar(
    message: String,
    type: SnackBar = SnackBar.NORMAL
) {
    showSnackbar(
        findViewById(android.R.id.content),
        message,
        getIcon(type, resources),
        getColor(type, resources)
    )
}

/**
 * -----------------------------------------
 * Helpers
 *
 */

enum class SnackBar {
    SUCCESS, ERROR, NORMAL
}

private fun getIcon(type: SnackBar, res: Resources): Drawable? {
    return when (type) {
        SnackBar.SUCCESS -> res.getDrawable(R.drawable.ic_success)
        SnackBar.ERROR -> res.getDrawable(R.drawable.ic_error)
        SnackBar.NORMAL -> null
    }
}

private fun getColor(type: SnackBar, res: Resources): Int {
    return res.getColor(
        when (type) {
            SnackBar.SUCCESS -> R.color.color_success
            SnackBar.ERROR -> R.color.color_error
            SnackBar.NORMAL -> R.color.color_normal
        }
    )
}

private fun showSnackbar(
    view: View,
    message: String,
    icon: Drawable?,
    color: Int
) {
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setBackgroundTint(color)

    if (icon != null)
        snackBar.setIcon(icon)

    snackBar.show()
}

private fun Snackbar.setIcon(drawable: Drawable): Snackbar {
    return this.apply {
        setAction(" ") {}
        val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
        textView.text = ""

        drawable.setTintMode(PorterDuff.Mode.SRC_ATOP)
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }
}
