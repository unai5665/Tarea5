package utils

import java.security.MessageDigest

fun sha512(text: String): String{
    val md = MessageDigest.getInstance("SHA-512")
    val digest = md.digest(text.toByteArray(Charsets.UTF_8))

    return digest.joinToString(separator = "") { "%02x".format(it) }
}