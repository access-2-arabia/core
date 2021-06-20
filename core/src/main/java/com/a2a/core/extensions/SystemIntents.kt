package com.a2a.core.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import java.util.*

fun Fragment.startAppSettingsIntent(requestCode: Int) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", this.context?.packageName, null)
    intent.data = uri
    if (intent.resolveActivity(requireContext().packageManager) != null) {
        startActivityForResult(intent, requestCode)
    }
}

fun Activity.startAppSettingsIntent(requestCode: Int) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(intent, requestCode)
    }
}

fun Fragment.startGoogleMapIntent(latLng: LatLng) {
    val url = "http://maps.google.com/maps?f=d&daddr=${latLng.latitude},${latLng.longitude}&dirflg=d&layer=t"
    val uri = String.format(Locale.ENGLISH, url)
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    if (intent.resolveActivity(requireContext().packageManager) != null) {
        startActivity(intent)
    }
}

fun Fragment.startDialIntent(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    if (intent.resolveActivity(requireContext().packageManager) != null) {
        startActivity(intent)
    }
}
