package  com.a2a.core.utility.biometricAuth

interface FingerPrintListener {
    fun onAuthenticationSucceeded()
    fun onFiler()
    fun onHelper(text: CharSequence)
}