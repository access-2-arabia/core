package com.a2a.core.bindingadapters

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.a2a.core.extensions.getLocal


@BindingAdapter(value = ["app:arabic","english"] ,requireAll = true)
fun AppCompatTextView?.getText(
    arabic: String,
    english: String
) {
    this?.text = english.getLocal(arabic)
}
