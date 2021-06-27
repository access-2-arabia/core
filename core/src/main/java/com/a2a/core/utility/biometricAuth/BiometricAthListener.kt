package  com.a2a.core.utility.biometricAuth

import java.io.Serializable

interface BiometricAthListener : Serializable {

    fun onExecute(type: Int)

}