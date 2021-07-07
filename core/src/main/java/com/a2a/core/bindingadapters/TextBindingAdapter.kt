package com.a2a.core.bindingadapters

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.a2a.core.extensions.getLocal


@BindingAdapter("getText")
fun AppCompatTextView?.getText(
    english: String,
    arabic: String
) {
    this?.text = english.getLocal(arabic)
}
