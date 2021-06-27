package  com.a2a.core.utility.biometricAuth


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.a2a.core.callbacks.CommunicationListener
import javax.inject.Inject


class BiometricPromptUtility  @Inject constructor() : CommunicationListener {
    //region variable

    companion object {
        var SUCCESS = 0
        var FAILURE = 1
    }

    //endregion
    @RequiresApi(Build.VERSION_CODES.P)
    fun prepare(
        context: Fragment, biometricAthListener: BiometricAthListener,
        title: String, cancelButtonText: String
    ) {

        val promptInfo = createPromptInfo(title, cancelButtonText)
        val biometricPrompt = createBiometricPrompt(context, biometricAthListener)
        execute(context, biometricPrompt, promptInfo, biometricAthListener)

    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun execute(
        context: Fragment,
        biometricPrompt: BiometricPrompt,
        promptInfo: BiometricPrompt.PromptInfo,
        biometricAthListener: BiometricAthListener
    ) {

        if (BiometricManager.from(context.requireContext())
                .canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
        ) {
            biometricPrompt.authenticate(promptInfo)
        } else {
            biometricAthListener.onExecute(FAILURE)
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
                biometricAthListener.onExecute(SUCCESS)
            }
        }

        return BiometricPrompt(context, executor, callback)
    }


    private fun createPromptInfo(
        title: String,
        cancelButtonText: String
    ): BiometricPrompt.PromptInfo {
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