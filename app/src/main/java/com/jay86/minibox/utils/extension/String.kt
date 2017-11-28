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