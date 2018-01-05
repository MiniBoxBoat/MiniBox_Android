package com.jay86.minibox.utils.extension

import java.math.BigInteger
import java.security.MessageDigest


/**
 * Created By jay68 on 2017/11/28.
 */
fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    md.update(toByteArray())
    return BigInteger(1, md.digest()).toString(16)
}

fun CharSequence.containsEmpty() = length == 0 || indices.any { this[it].isWhitespace() }

fun CharSequence.isPhoneNumber() = isChinaPhoneNumber() || isHKPhoneNumber()

fun CharSequence.isChinaPhoneNumber()
        = matches(Regex("^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$"))

fun CharSequence.isHKPhoneNumber() = matches(Regex("^(5|6|8|9)\\d{7}$"))