package com.a2a.core.utility

import android.annotation.SuppressLint
import android.provider.Settings
import androidx.fragment.app.FragmentActivity

object DeviceUtils {

    @SuppressLint("HardwareIds")
    fun getDeviceID(activity: FragmentActivity): String =
        Settings.Secure.getString(activity.contentResolver, Settings.Secure.ANDROID_ID)

}