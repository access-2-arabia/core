package  com.a2a.core.utility.biometricAuth

import java.io.Serializable

interface BiometricCancelListener : Serializable {

    fun onCancelClick()

}