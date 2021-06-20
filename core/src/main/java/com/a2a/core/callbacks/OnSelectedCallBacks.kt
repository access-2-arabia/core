package com.a2a.core.callbacks

import java.io.Serializable

interface OnSelectedCallBacks  : Serializable {
    fun <T> selectedCallBacks(selectedObject: T)
}