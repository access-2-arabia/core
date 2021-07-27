package com.a2a.core.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.SearchView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.a2a.core.constants.DateTime
import com.a2a.core.constants.DefaultValues.DEBOUNCE_SEARCH_DELAY
import com.a2a.core.constants.DefaultValues.DEFAULT_INT
import com.a2a.core.constants.StringCharacters
import com.a2a.core.constants.StringCharacters.EMPTY_STRING
import com.a2a.core.ui.date.DateTimeDialog
import com.a2a.core.ui.date.DateTimeLimitation
import com.a2a.core.ui.date.DialogType
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

inline fun SearchView.doOnDebounceQueryChange(
    lifecycleOwner: LifecycleOwner,
    crossinline action: (query: String?) -> Unit
) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        private var searchFor = EMPTY_STRING

        override fun onQueryTextSubmit(query: String?): Boolean {
            action(query)
            clearFocus()
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            val searchText = newText.toString().trim()
            if (searchText == searchFor)
                return true

            searchFor = searchText

            lifecycleOwner.lifecycleScope.launch {
                delay(DEBOUNCE_SEARCH_DELAY)
                if (searchText != searchFor)
                    return@launch
                action(newText)
            }
            return true
        }
    })
}

inline fun EditText.doOnDebounceQueryChange(
    lifecycleOwner: LifecycleOwner,
    crossinline action: (query: String?) -> Unit
) {
    var searchFor = EMPTY_STRING
    doOnTextChanged { text, _, _, _ ->
        val searchText = text.toString().trim()
        if (searchText == searchFor)
            return@doOnTextChanged

        searchFor = searchText

        lifecycleOwner.lifecycleScope.launch {
            delay(DEBOUNCE_SEARCH_DELAY)
            if (searchText != searchFor)
                return@launch
            action(text?.toString())
        }
    }
}

fun View.screenshot(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)
    return bitmap
}

fun Int.screenshot(layoutInflater: LayoutInflater, width: Float, height: Float = width): Bitmap {
    return toMeasuredView(layoutInflater, false, width, height).screenshot()
}

fun Int.toMeasuredView(
    layoutInflater: LayoutInflater,
    authSize: Boolean = false,
    width: Float = 0f,
    height: Float = width
): View {
    val parent = LinearLayout(layoutInflater.context)
    val inflatedFrame: View = layoutInflater.inflate(this, parent, false)
    val params: ViewGroup.LayoutParams = inflatedFrame.layoutParams

    val wSpec: Int = measureSpecFromDimension(params.width, width.toInt())
    val hSpec: Int = measureSpecFromDimension(params.height, height.toInt())
    inflatedFrame.measure(wSpec, hSpec)


    if (authSize) {
        val measuredWidth = inflatedFrame.measuredWidth
        val measuredHeight = inflatedFrame.measuredHeight
        inflatedFrame.layout(DEFAULT_INT, DEFAULT_INT, measuredWidth, measuredHeight)
    } else {
        inflatedFrame.layout(DEFAULT_INT, DEFAULT_INT, width.toInt(), height.toInt())
    }

    return inflatedFrame
}

fun ImageView.load64Image(image: String, context: Context) {
    val decodedString = Base64.decode(image, Base64.DEFAULT)
    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    Glide.with(context).load(decodedByte).into(this)
}

fun measureSpecFromDimension(dimension: Int, maxDimension: Int): Int {
    return when (dimension) {
        ViewGroup.LayoutParams.MATCH_PARENT -> View.MeasureSpec.makeMeasureSpec(
            maxDimension,
            View.MeasureSpec.EXACTLY
        )
        ViewGroup.LayoutParams.WRAP_CONTENT -> View.MeasureSpec.makeMeasureSpec(
            maxDimension,
            View.MeasureSpec.AT_MOST
        )
        else -> View.MeasureSpec.makeMeasureSpec(dimension, View.MeasureSpec.EXACTLY)
    }
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Fragment.getDrawable(id: Int) = AppCompatResources.getDrawable(requireContext(), id)

fun AutoCompleteTextView.setListAsString(
    list: List<String>,
    listener: AdapterView.OnItemClickListener
) {
    setAdapter(ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, list))
    onItemClickListener = listener
}

fun AppCompatEditText.showDateTimeDialog(
    fragment: FragmentActivity,
    type: DialogType,
    data: String = StringCharacters.EMPTY_STRING,
    dateLimit: DateTimeLimitation = DateTimeLimitation.NONE
) {
    if (data.isNotEmpty()) setText(data)
    setOnClickListener {
        DateTimeDialog(fragment, type, data, dateLimit) {
            setTextFromCalender(it, type)
        }
    }
}

fun AppCompatEditText.setTextFromCalender(calendar: Calendar, type: DialogType) {
    val format = when (type) {
        DialogType.Date -> DateTime.FORMAT_DATE
        DialogType.Time -> DateTime.FORMAT_TIME
    }
    val sdf = SimpleDateFormat(format, Locale.ENGLISH)
    setText(sdf.format(calendar.time))
}
