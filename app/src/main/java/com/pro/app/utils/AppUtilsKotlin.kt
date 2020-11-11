package com.pro.app.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AppUtilsKotlin {
    companion object {

        fun formatString(text: String): String {

            val json = StringBuilder()
            var indentString = ""

            for (i in 0 until text.length) {
                val letter = text[i]
                when (letter) {
                    '{', '[' -> {
                        json.append("\n" + indentString + letter + "\n")
                        indentString += "\t"
                        json.append(indentString)
                    }
                    '}', ']' -> {
                        indentString = indentString.replaceFirst("\t".toRegex(), "")
                        json.append("\n" + indentString + letter)
                    }
                    ',' -> json.append(letter + "\n" + indentString)

                    else -> json.append(letter)
                }
            }

            return json.toString()
        }
    }
}