package com.paddy.bookseatapp.utils

import android.content.Context
import android.view.View
import android.widget.Toast

fun View?.show() {
    this?.visibility = View.VISIBLE
}

fun View?.hide() {
    this?.visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun String?.isNotNullOrEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}

fun Any?.isNotNull(): Boolean {
    return this != null
}

fun Context.showMessage(msg : String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}