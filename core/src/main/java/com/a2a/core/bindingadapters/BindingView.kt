package com.a2a.core.bindingadapters

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:onLongClick")
fun setOnLongClickListener(view: View, func: () -> Unit) {
    view.setOnLongClickListener {
        func()
        return@setOnLongClickListener true
    }
}