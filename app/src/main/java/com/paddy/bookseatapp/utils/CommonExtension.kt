package com.paddy.bookseatapp.utils

import java.math.BigDecimal

fun Long.appendZero() : String{
    return if (this >= 10) {
        this.toString()
    } else {
        "0".plus(this.toString());
    }
}

fun BigDecimal.priceFormatting() : String {
    return String.format("%.1f", this)
}