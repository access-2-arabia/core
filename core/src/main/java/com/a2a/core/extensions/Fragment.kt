package com.a2a.core.extensions

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigateAction(@IdRes id: Int, bundle: Bundle? = null) {
    bundle?.let { findNavController().navigate(id, it) }
        ?: run { findNavController().navigate(id) }
}

fun Fragment.popToPage(@IdRes id: Int = 0) {
    if (id.isNotZero()) findNavController().navigate(id)
    else findNavController().popBackStack()
}

fun Fragment.customBackButton(onClick: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onClick.invoke()
            }
        })
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}