package com.paddy.bookseatapp.domain

import com.google.gson.Gson
import com.paddy.bookseatapp.data.model.LibraryQRScanResult
import org.apache.commons.text.StringEscapeUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryDataParser @Inject constructor() {

    fun parseUnformattedJson(unformattedJson: String): LibraryQRScanResult {
        val unQuotedString = unformattedJson.replace("^\"|\"$".toRegex(), "")
        val unescapeString = StringEscapeUtils.unescapeJava(unQuotedString)
        return Gson().fromJson(unescapeString, LibraryQRScanResult::class.java)
    }
}