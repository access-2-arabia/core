package com.a2a.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.a2a.core.utility.SafeClickListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
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
    return if (Locale.getDefault().language == "en") this?:"" else ar?:""
}

fun Activity.changeStatusBarColor(color:Int){
    window.statusBarColor = ContextCompat.getColor(this,color)
}

fun FragmentManager.getCurrentNavigationFragment(): Fragment? =
    primaryNavigationFragment?.childFragmentManager?.fragments?.first()









