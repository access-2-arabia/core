package com.a2a.core.utility

import android.content.res.Resources

fun getCustomWidth(percentage: Double) =
    (Resources.getSystem().displayMetrics.widthPixels * percentage).toInt()

