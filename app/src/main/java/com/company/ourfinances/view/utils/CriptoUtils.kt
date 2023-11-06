package com.company.ourfinances.view.utils

import android.util.Base64
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

private const val SALT = "4631241932312dasasda12312"
private const val PASSWORD = "341p8966432413890312321122ggdfdfg34134"
class CryptoUtils {
    private val salt = SALT
    private val password = PASSWORD

    fun encrypt(cardNumber: String): String {
        try {
            val keySpec: KeySpec = PBEKeySpec(password.toCharArray(), salt.toByteArray(), 212, 256) // Geração da chave secreta
            val secretKeyFactory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val keyBytes: ByteArray = secretKeyFactory.generateSecret(keySpec).encoded
            val secretKey: SecretKey = SecretKeySpec(keyBytes, "AES")

            val cipher: Cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            val encryptedTextBytes: ByteArray = cipher.doFinal(cardNumber.toByteArray())

            return Base64.encodeToString(encryptedTextBytes, Base64.DEFAULT)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }
        return ""
    }

    fun decrypt(encryptedCardNumber: String): String {
        try {
            val encryptedTextBytes: ByteArray = Base64.decode(encryptedCardNumber, Base64.DEFAULT)

            val keySpec: KeySpec = PBEKeySpec(password.toCharArray(), salt.toByteArray(), 212, 256) // Geração da chave secreta
            val secretKeyFactory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val keyBytes: ByteArray = secretKeyFactory.generateSecret(keySpec).encoded
            val secretKey: SecretKey = SecretKeySpec(keyBytes, "AES")

            val cipher: Cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)

            val decryptedTextBytes: ByteArray = cipher.doFinal(encryptedTextBytes)

            return String(decryptedTextBytes)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }
        return ""
    }
}
