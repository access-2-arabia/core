package  com.a2a.core.utility.biometricAuth


import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FingerPrintUtility  @Inject constructor(private var helper:FingerprintHelper) {
    //region variable
    private lateinit var cipher: Cipher
    private lateinit var fingerPrintListener: FingerPrintListener
    lateinit var keyStore: KeyStore
    lateinit var fingerprintManager: FingerprintManager
    lateinit var biometricPrompt: BiometricPrompt



    //endregion

    @RequiresApi(Build.VERSION_CODES.M)
    fun prepare(context: Context, fingerPrintListener: FingerPrintListener) {
        this.fingerPrintListener = fingerPrintListener
        if (checkLockScreen(context)) {
            generateKey()
            if (initCipher()) {
                execute(context, fingerPrintListener)
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun stopFingerAuth() {
        helper.stopFingerAuth()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun execute(context: Context, fingerPrintListener: FingerPrintListener) {

        helper = FingerprintHelper(context, fingerPrintListener)
        cipher.let {
            val cryptoObject = FingerprintManager.CryptoObject(it)
            helper.startAuth(fingerprintManager, cryptoObject)

        }

    }


    fun isFingerHardwareNotDetected(context: Context): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((context.getSystemService(Context.FINGERPRINT_SERVICE) as? (FingerprintManager)) != null) {
                fingerprintManager =
                    (context.getSystemService(Context.FINGERPRINT_SERVICE) as? (FingerprintManager))!!
                if (!fingerprintManager.isHardwareDetected) {
                    // Device doesn't support fingerprint authentication
                    return "HardWareNotSupport"
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    return "NoFingerPrint"

                }
            } else {
                return "DontHaveFingerPrint"
            }
            //Fingerprint API only available on from Android 6.0 (M)
        }
        return "Yes"
    }

    private fun checkLockScreen(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val keyguardManager: KeyguardManager =
                context.getSystemService(Context.KEYGUARD_SERVICE)
                        as KeyguardManager
            fingerprintManager =
                context.getSystemService(Context.FINGERPRINT_SERVICE)
                        as FingerprintManager
            if (!keyguardManager.isKeyguardSecure) {

                fingerPrintListener.onHelper("Lock screen security not enabled")
                return false
            }

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.USE_FINGERPRINT
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                fingerPrintListener.onHelper("Permission not enabled (Fingerprint)")
                return false
            }

            if (!fingerprintManager.hasEnrolledFingerprints()) {
                fingerPrintListener.onHelper("No fingerprint registered, please register")
                return false
            }
            return true
        }
        return false

    }

    private fun generateKey() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            lateinit var keyGenerator: KeyGenerator

            try {
                keyStore = KeyStore.getInstance("AndroidKeyStore")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore"
                )
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException(
                    "Failed to get KeyGenerator instance", e
                )
            } catch (e: NoSuchProviderException) {
                throw RuntimeException("Failed to get KeyGenerator instance", e)
            }

            try {
                keyStore.load(null)
                keyGenerator.init(
                    KeyGenParameterSpec.Builder(
                        "KEY_NAME",
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7
                        )
                        .build()
                )

                keyGenerator.generateKey()
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException(e)
            } catch (e: InvalidAlgorithmParameterException) {
                throw RuntimeException(e)
            } catch (e: CertificateException) {
                throw RuntimeException(e)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    private fun initCipher(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            try {
                cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7
                )
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException("Failed to get Cipher", e)
            } catch (e: NoSuchPaddingException) {
                throw RuntimeException("Failed to get Cipher", e)
            }

            try {
                keyStore.load(null)
                val key = keyStore.getKey("KEY_NAME", null) as SecretKey
                cipher.init(Cipher.ENCRYPT_MODE, key)
                return true
            } catch (e: KeyPermanentlyInvalidatedException) {
                return false
            } catch (e: KeyStoreException) {
                throw RuntimeException("Failed to init Cipher", e)
            } catch (e: CertificateException) {
                throw RuntimeException("Failed to init Cipher", e)
            } catch (e: UnrecoverableKeyException) {
                throw RuntimeException("Failed to init Cipher", e)
            } catch (e: IOException) {
                throw RuntimeException("Failed to init Cipher", e)
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException("Failed to init Cipher", e)
            } catch (e: InvalidKeyException) {
                throw RuntimeException("Failed to init Cipher", e)
            }
        }
        return false
    }

}