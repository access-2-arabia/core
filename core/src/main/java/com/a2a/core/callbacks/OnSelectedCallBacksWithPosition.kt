package com.a2a.core.callbacks

import java.io.Serializable

interface OnSelectedCallBacksWithPosition : Serializable {
    fun <T> selectedCallBacks(selectedObject: T, position: Int)
}