package com.a2a.core.callbacks

import java.io.Serializable

interface OnSelectedWithTypeCallBacks : Serializable {
    fun <T> onExecute(type: String, selectedObject: T)
}