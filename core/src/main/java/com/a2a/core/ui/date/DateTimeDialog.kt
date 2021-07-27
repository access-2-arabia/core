package com.a2a.core.ui.date


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import com.a2a.core.extensions.toCalendarDate

import java.util.*

class DateTimeDialog(
    private val context: Context,
    type: DialogType,
    private val data: String,
    dateLimit: DateTimeLimitation,
    var listener: (calendar: Calendar) -> Unit
) {
    private val calendar = Calendar.getInstance()


    init {
        when (type) {
            DialogType.Date -> createDateDialog(dateLimit)
            DialogType.Time -> createTimeDialog()
        }
    }


    private fun createDateDialog(dateLimit: DateTimeLimitation) {

        var years = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)

        if (data.isNotEmpty()) {
            val date = data.toCalendarDate()
            years = date.get(Calendar.YEAR)
            month = date.get(Calendar.MONTH)
            day = date.get(Calendar.DAY_OF_MONTH)
        }


        val datePickerDialog = DatePickerDialog(context, { _, year, monthOfYear, dayOfMonth ->
            listener.invoke(Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, monthOfYear)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            })
        }, years, month, day)
        when (dateLimit) {
            DateTimeLimitation.CURRENT -> datePickerDialog.datePicker.minDate =
                System.currentTimeMillis() - 1000
            DateTimeLimitation.ADULT_18 -> {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.YEAR, -18)
                datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            }
            DateTimeLimitation.NONE -> Unit
        }

        datePickerDialog.show()
    }

    private fun createTimeDialog() {
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        var min = calendar.get(Calendar.MINUTE)

        if (data.isNotEmpty()) {
            val date = data.toCalendarDate()
            hour = date.get(Calendar.HOUR_OF_DAY)
            min = date.get(Calendar.MINUTE)
        }

        val timePickerDialog = TimePickerDialog(context, { _, hourOfDay, minute ->
            listener.invoke(Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            })
        }, hour, min, false)
        timePickerDialog.show()
    }
}