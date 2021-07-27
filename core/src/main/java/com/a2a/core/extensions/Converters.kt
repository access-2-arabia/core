package com.a2a.core.extensions

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import com.a2a.core.constants.DateTime
import com.a2a.core.constants.DefaultValues.DEFAULT_INT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Int.toDp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Long.millisecondsToTime(): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(minutes)
    return String.format(DateTime.FORMAT_MILLISECONDS_TO_TIME, minutes, seconds)
}

fun Int.isNotZero(): Boolean = !this.isZero()

fun Int.isZero(): Boolean = this == DEFAULT_INT

fun String.toCalendarDate(): Calendar {
    try {
        val date = SimpleDateFormat(DateTime.FORMAT_DATE, Locale.ENGLISH).parse(this)
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    } catch (e: ParseException) {
        throw e
    }
}

fun String.toHTML(): Spanned? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
    Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT) else Html.fromHtml(this)