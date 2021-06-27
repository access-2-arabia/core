package  com.a2a.core.utility.biometricAuth


 import android.os.Build
import androidx.annotation.RequiresApi
 import androidx.biometric.BiometricManager
 import androidx.biometric.BiometricPrompt

 import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import androidx.navigation.fragment.findNavController
 import com.a2a.core.R
 import com.a2a.core.callbacks.CommunicationListener


class BiometricPromptUtility : CommunicationListener {
    //region variable

    companion object {
        var instance = BiometricPromptUtility()
    }
    //endregion
    @RequiresApi(Build.VERSION_CODES.P)
    fun prepare(context: Fragment, biometricAthListener: BiometricAthListener,
                title:String,cancelButtonText:String) {

        val promptInfo = createPromptInfo(title,cancelButtonText)
        val biometricPrompt = createBiometricPrompt(context, biometricAthListener)
        execute(context, biometricPrompt, promptInfo)

    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun execute(
        context: Fragment,
        biometricPrompt: BiometricPrompt,
        promptInfo: BiometricPrompt.PromptInfo
    ) {

        if (BiometricManager.from(context.requireContext())
                .canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
        ) {
            biometricPrompt.authenticate(promptInfo)
        } else {
//            val action = LoginFragmentDirections.toWarningFragment(
//                context.getString(R.string.please_enable_biomtriec),
//                this
//            )
//            context.findNavController().navigate(action)
        }
    }


    private fun createBiometricPrompt(
        context: Fragment,
        biometricAthListener: BiometricAthListener
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(context.requireContext())

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                biometricAthListener.onExecute(0)
            }
        }

        return BiometricPrompt(context, executor, callback)
    }


    private fun createPromptInfo(title:String,cancelButtonText:String): BiometricPrompt.PromptInfo {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            // Authenticate without requiring the user to press a "confirm"
            // button after satisfying the biometric check
            .setConfirmationRequired(false)
            .setNegativeButtonText(cancelButtonText)
            .build()
        return promptInfo
    }

    override fun onExecute() {

    }


}