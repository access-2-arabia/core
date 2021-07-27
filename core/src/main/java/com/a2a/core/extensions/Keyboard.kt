package com.a2a.core.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Activity.hideKeyboard() {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
    if (currentFocus != null) {
        inputManager?.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun Fragment.hideKeyboard() {
    requireActivity().hideKeyboard()
}