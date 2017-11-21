package com.jay86.minibox.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created By jay68 on 2017/11/21.
 */
fun ImageView.setImageUrl(url: String?, placeHolder: Drawable? = null, errorHolder: Drawable? = null) {
    Glide.with(context)
            .load(url)
            .placeholder(placeHolder)
            .error(errorHolder)
            .into(this)
}