package com.a2a.core.extensions

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import com.a2a.core.constants.DateTime
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun Date.formatToServerDateTimeDefaults(): String {
    val sdf = SimpleDateFormat(DateTime.FORMAT_TO_SERVER_DATE_TIME, Locale.getDefault())
    return sdf.format(this)
}

fun TextView.showDateDialog(context: Context) {
    val newCalendar = Calendar.getInstance()
    val newDate = Calendar.getInstance()
    val mDateFromPicker = DatePickerDialog(
        context, { _, year, monthOfYear, dayOfMonth ->
            newDate[year, monthOfYear] = dayOfMonth
            text = newDate.time.formatToDefaults()

        },
        newCalendar[Calendar.YEAR],
        newCalendar[Calendar.MONTH],
        newCalendar[Calendar.DAY_OF_MONTH]
    )
    mDateFromPicker.datePicker.maxDate = Date().time
    mDateFromPicker.show()
}

fun Date.formatToTruncatedDateTime(): String {
    val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    return sdf.format(this)
}


fun Date.formatToServerDateDefaults(): String {
    val sdf = SimpleDateFormat(DateTime.FORMAT_TO_SERVER_DATE, Locale.getDefault())
    return sdf.format(this)
}

fun Date.formatDayMonthYearServerDateDefaults(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return sdf.format(this)
}


fun Date.formatToServerTimeDefaults(): String {
    val sdf = SimpleDateFormat(DateTime.FORMAT_TO_SERVER_TIME, Locale.getDefault())
    return sdf.format(this)
}

fun Date.formatToViewDateTimeDefaults(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(this)
}


fun Date.formatToDefaults(): String {
    val sdf = SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
    return sdf.format(this)
}

fun Date.formatToAPI(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    return sdf.format(this)
}

fun Date.formatCliQToDefaults(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    return sdf.format(this)
}

fun String.formatTimeZoneDefaults(): String {

    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val dateParse: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    try {
        var date = Date()
        date = if (this.contains("T"))
            dateParse.parse(this.substringBefore('T'))
        else
            dateParse.parse(this)

        return dateFormat.format(date.time)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun Date.getDaysAgo(daysAgo: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)

    return calendar.time
}

fun Date.getMonthsAgo(monthAgo: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, -monthAgo)

    return calendar.time
}

fun Date.dayPlusYear(year: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.YEAR, +year)

    return calendar.time
}

fun Date.formatToGropingDate(): String {
    val sdf = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())
    return sdf.format(this)
}


fun Date.formatToDayName(): String {
    val sdf = SimpleDateFormat("E", Locale.getDefault())
    return sdf.format(this)
}


fun Date.formatToViewDateDefaults(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    return sdf.format(this)
}

fun Date.formatToViewDateDefaults(format: String): String {
    val sdf = SimpleDateFormat(format, Locale.ENGLISH)
    return sdf.format(this)
}


fun Date.formatDayMonth(): String {
    val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
    return sdf.format(this)
}

fun Date.formatSacendToMinet(): String {

    val sdf = SimpleDateFormat("mm:ss", Locale.getDefault())
    return sdf.format(this)
}


fun Date.formatToViewTimeDefaults(): String {
    val sdf = SimpleDateFormat("HH:mm aa", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Add field date to current date
 */
fun Date.add(field: Int, amount: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(field, amount)

    this.time = cal.time.time

    cal.clear()

    return this
}

fun Date.addYears(years: Int): Date {
    return add(Calendar.YEAR, years)
}

fun Date.addMonths(months: Int): Date {
    return add(Calendar.MONTH, months)
}

fun Date.addDays(days: Int): Date {
    return add(Calendar.DAY_OF_MONTH, days)
}

fun Date.addHours(hours: Int): Date {
    return add(Calendar.HOUR_OF_DAY, hours)
}

fun Date.addMinutes(minutes: Int): Date {
    return add(Calendar.MINUTE, minutes)
}

fun Date.addSeconds(seconds: Int): Date {
    return add(Calendar.SECOND, seconds)
}

fun Date.formatToViewDayDefaults(): String {

    val sdf = SimpleDateFormat("dd", Locale.ENGLISH)
    return sdf.format(this)
}


fun String.formatTime(): String {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US)
    val dateParse: DateFormat = SimpleDateFormat("hh:mm a", Locale.US)
    try {
        val dte = dateFormat.parse(this)
        return dateParse.format(dte.time)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun String.formatDateAndTime(): String {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US)
    val dateParse: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US)
    try {
        val dte = dateFormat.parse(this)
        return dateParse.format(dte.time)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}






