package com.a2a.core.extensions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Context.intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(addressableActivity.action).setPackage(packageName)
}

fun Fragment.intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(addressableActivity.action).setPackage(requireContext().packageName)
}

interface AddressableActivity {
    val action: String
}


