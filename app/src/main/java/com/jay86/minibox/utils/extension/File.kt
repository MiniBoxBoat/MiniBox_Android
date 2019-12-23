package com.jay86.minibox.utils.extension

import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import com.jay86.minibox.App
import java.io.File
import java.io.IOException

/**
 * Created by Jay
 */
fun File.reset(): File {
    try {
        if (exists()) {
            delete()
        }
        createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        return this
    }
}

val File.uri: Uri
    get() = if (Build.VERSION.SDK_INT >= 24) {
        FileProvider.getUriForFile(App.context, "com.jay86.minibox.fileprovider", this)
    } else {
        Uri.fromFile(this)
    }