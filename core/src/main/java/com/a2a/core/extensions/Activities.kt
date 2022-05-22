package com.a2a.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

fun Activity.intentTo(toPage: Class<*>, isFinishCurrentPage: Boolean = true) {
    val loginIntent = Intent(this, toPage)
    startActivity(loginIntent)
    if (isFinishCurrentPage) finish()
}

fun Context.intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(addressableActivity.action).setPackage(packageName)
}

fun Fragment.intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(addressableActivity.action).setPackage(requireContext().packageName)
}

interface AddressableActivity {
    val action: String
}


