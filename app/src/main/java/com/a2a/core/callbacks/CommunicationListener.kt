package com.a2a.core.callbacks

import java.io.Serializable

interface CommunicationListener : Serializable {
    fun onExecute()
}