package  com.a2a.core.utility.biometricAuth

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat


@RequiresApi(Build.VERSION_CODES.M)
class FingerprintHelper(private val appContext: Context, private var fingerPrintListener: FingerPrintListener) :
    FingerprintManager.AuthenticationCallback() {

    private lateinit var cancellationSignal: CancellationSignal


    fun stopFingerAuth() {
        if (!cancellationSignal.isCanceled) {
            cancellationSignal.cancel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun startAuth(
        manager: FingerprintManager,
        cryptoObject: FingerprintManager.CryptoObject
    ) {

        cancellationSignal = CancellationSignal()

        if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.USE_FINGERPRINT
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null)
    }

    override fun onAuthenticationError(
        errMsgId: Int,
        errString: CharSequence
    ) {
        fingerPrintListener.onHelper(errString)

    }

    override fun onAuthenticationHelp(
        helpMsgId: Int,
        helpString: CharSequence
    ) {
        fingerPrintListener.onHelper(helpString)
    }

    override fun onAuthenticationFailed() {
        fingerPrintListener.onFiler()
    }

    override fun onAuthenticationSucceeded(
        result: FingerprintManager.AuthenticationResult
    ) {

        fingerPrintListener.onAuthenticationSucceeded()

    }



}