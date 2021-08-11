package com.a2a.core.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.a2a.core.utility.*
import com.scottyab.rootbeer.RootBeer
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}


fun checkRootDevice(context: Context): Boolean = RootBeer(context).isRooted


fun EditText.showKeyboard(context: Context) {
    requestFocus()
    postDelayed(Runnable {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }, 200)
}

fun String.isValidNumber(): Boolean {
    return this.startsWith("0096279")
            || this.startsWith("0096277")
            || this.startsWith("0096278")

}

fun String.mobileFormat(): String {
    var mobileNumber = this
    when {
        mobileNumber.startsWith("962") -> {
            mobileNumber = "00$mobileNumber"
        }
        mobileNumber.startsWith("7") -> {
            mobileNumber = "00962$mobileNumber"
        }
        mobileNumber.startsWith("07") -> {
            mobileNumber = "00962${mobileNumber.removeRange(0, 1)}"
        }
    }
    return mobileNumber
}

fun String.mobileFormatWithoutCode(): String {
    var mobileNumber = this
    when {
        mobileNumber.startsWith("07") -> {
            mobileNumber = mobileNumber
        }
        mobileNumber.startsWith("7") -> {
            mobileNumber = "962$mobileNumber"
        }
        mobileNumber.startsWith("96207") -> {
            mobileNumber = mobileNumber.removeRange(0, 3)
        }
        mobileNumber.startsWith("962") -> {
            mobileNumber = mobileNumber
        }
    }
    return mobileNumber
}

fun String.notZero(): Boolean {
    if (this == "." || this == "," || isNullOrEmpty())
        return false

    val double = this.toDouble()
    return double >= 0.000001

}

fun EditText.doValidation(length: Int): Boolean {
    return this.text.length >= length

}

fun String.formatDate(): String {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val dateParse: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    try {
        val dte = dateParse.parse(this)
        return dateFormat.format(dte.time)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

fun File.convertToBase64(): String {

    val bm = BitmapFactory.decodeFile(absolutePath)
    val baos = ByteArrayOutputStream()
    bm.compress(
        Bitmap.CompressFormat.JPEG,
        10,
        baos
    ) //bm is the bitmap object


    val byteArrayImage: ByteArray = baos.toByteArray()
    var result = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    return result

}

fun String.convertToBoolean(): Boolean = this == "1"
fun Boolean.convertToString(): String = if (this) "1" else "0"

fun shareQRImage(bitmap: Bitmap, context: Context) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_STREAM, bitmap)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.type = "image/*"
    context.startActivity(intent)
}

fun String.isValidIban(): Boolean {
    if (!"^[0-9A-Z]*\$".toRegex().matches(this)) {
        return false
    }
    val symbols = this.trim { it <= ' ' }
    if (symbols.length < 15 || symbols.length > 34) {
        return false
    }
    val swapped = symbols.substring(4) + symbols.substring(0, 4)
    return swapped.toCharArray()
        .map { it.toInt() }
        .fold(0) { previousMod: Int, _char: Int ->
            val value = Integer.parseInt(Character.toString(_char.toChar()), 36)
            val factor = if (value < 10) 10 else 100
            (factor * previousMod + value) % 97
        } == 1
}

fun String.shareText(context: Context, shareMessage: String) {
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(
        Intent.EXTRA_TEXT,
        shareMessage
    )
    sendIntent.type = "text/plain"
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}


fun String?.getLocal(ar: String?): String {
    return if (Locale.getDefault().language == "en") this ?: "" else ar ?: ""
}

fun Activity.changeStatusBarColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = ContextCompat.getColor(this, color)
    }
}

fun FragmentManager.getCurrentNavigationFragment(): Fragment? =
    primaryNavigationFragment?.childFragmentManager?.fragments?.first()


fun EditText.getString() = this.text.toString().trim()


fun showViews(vararg view: View) {
    view.forEach {
        it.visible(true)
    }
}

fun hideViews(vararg view: View) {
    view.forEach {
        it.visible(false)
    }
}


fun enableViews(vararg view: View) {
    view.forEach {
        it.enable(true)
    }
}

fun disableViews(vararg view: View) {
    view.forEach {
        it.enable(false)
    }
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(isEnable: Boolean) {
    isEnabled = isEnable
}

fun String.copyText(context: Context, successMessage: String) {
    val myClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val myClip: ClipData = ClipData.newPlainText("Label", this)
    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
    myClipboard.setPrimaryClip(myClip)
}

fun String.stringToDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).parse(this) ?: Date()
}

fun String.stringToDateyyymmdd(): Date {
    return SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(this) ?: Date()
}

fun String.stringToTime(): Date {
    return SimpleDateFormat("HH:mm:ss.SSS", Locale.ENGLISH).parse(this) ?: Date()
}

fun String.getDoubleFromString(): Double {
    var convertedNumber = this
    if (this.contains(","))
        convertedNumber = this.replace(",", "")
    return convertedNumber.toDouble()
}

//29/01/2019
fun String.formatToDefaults(): String {
    val sdf = SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
    return sdf.format(this)
}

//@SuppressLint("SetTextI18n")
//fun TextView.formatAmount(value: String) {
//    if (value.isNotEmpty()) {
//        var convertedNumber = value
//        if (value.contains(","))
//            convertedNumber = value.replace(",", "")
//        text = String.format(
//            "%,.3f",
//            convertedNumber.toDouble()
//        ) + " " + Constants.Currency.currency.getLocal(Constants.Currency.currencyAr)
//    }
//}

@SuppressLint("SetTextI18n")
fun TextView.formatAmountWithCurrency(value: String, currency: String) {
    if (value.isNotEmpty()) {
        var convertedNumber = value
        if (value.contains(","))
            convertedNumber = value.replace(",", "")
        text = String.format(
            "%,.3f",
            convertedNumber.toDouble()
        ) + " " + currency
    }
}

@SuppressLint("SetTextI18n")
fun TextView.formatDecimalWithPercentage(value: Double) {
    text = String.format("%,.3f", value) + "%"
}

//@SuppressLint("SetTextI18n")
//fun TextView.formatAmount(value: String, currency: String) {
//    var foundCurrency = false
//    var convertedNumber = value
//    if (value.contains(","))
//        convertedNumber = value.replace(",", "")
//    if (currencyFormatList.isEmpty().not() && currency.isNotEmpty()) {
//        currencyFormatList.forEach {
//            if (it.iSOCode == currency) {
//                foundCurrency = true
//                val df = truncateTo(convertedNumber.toDouble(), it.cED.toInt())
//                this.text = "$df $currency"
//            }
//        }
//    } else {
//        this.text = String.format(
//            "%,.3f",
//            convertedNumber.toDouble()
//        ) + " " + currency
//    }
//    if (foundCurrency.not())
//        this.text = String.format("%,.3f", convertedNumber.toDouble()) + " " + currency
//}

fun truncateTo(unroundedNumber: Double, decimalPlaces: Int): Double {
    val truncatedNumberInt =
        (unroundedNumber * 10.0.pow(decimalPlaces.toDouble())).toInt()
    return (truncatedNumberInt / 10.0.pow(decimalPlaces.toDouble()))
}

fun RecyclerView.attachSnapHelperWithListener(
    snapHelper: SnapHelper,
    behavior: SnapOnScrollListener.Behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
    onSnapPositionChangeListener: OnSnapPositionChangeListener,
) {
    onFlingListener = null
    snapHelper.attachToRecyclerView(this)
    val snapOnScrollListener =
        SnapOnScrollListener(snapHelper, behavior, onSnapPositionChangeListener)
    addOnScrollListener(snapOnScrollListener)
}


fun RecyclerView.attachSnapHelperToAccountWithListener(
    snapHelper: SnapHelper,
    behavior: SnapOnScrollToAccountListener.Behavior = SnapOnScrollToAccountListener.Behavior.NOTIFY_ON_SCROLL,
    onSnapPositionChangeListener: OnSnapPositionChangeToAccountListener,
) {
    onFlingListener = null
    snapHelper.attachToRecyclerView(this)
    val snapOnScrollListener =
        SnapOnScrollToAccountListener(snapHelper, behavior, onSnapPositionChangeListener)
    addOnScrollListener(snapOnScrollListener)
}

fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}


fun toggleSection(bt: View, lyt: View) {
    val show = toggleArrow(bt)
    if (show) {
        ViewAnimation.expand(lyt, object : ViewAnimation.AnimListener {
            override fun onFinish() {

            }
        })
    } else {
        ViewAnimation.collapse(lyt)
    }
}

fun toggleArrow(view: View): Boolean {
    return if (view.rotation == 0f) {
        view.animate().setDuration(200).rotation(180f)
        true
    } else {
        view.animate().setDuration(200).rotation(0f)
        false
    }
}

fun getBasicFormat(): SimpleDateFormat {
    val basicFormat = "yyyy/MM/dd"
    return SimpleDateFormat(basicFormat, Locale.US)
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun Fragment.browsLink(link: String) {
    val browserIntent =
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(link)
        )
    startActivity(browserIntent)
}

fun Fragment.callNumber(number: String) {

    if (Build.VERSION.SDK_INT > 22) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                101
            )

        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$number")
            startActivity(callIntent)
        }

    } else {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$number")
        startActivity(callIntent)
    }

}

fun Fragment.sendEmail(email: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "plain/text"

    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
    startActivity(Intent.createChooser(intent, ""))
}


