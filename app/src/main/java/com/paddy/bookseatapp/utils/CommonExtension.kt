package com.paddy.bookseatapp.utils

fun Long.appendZero() : String{
    return if (this >= 10) {
        this.toString()
    } else {
        "0".plus(this.toString());
    }
}