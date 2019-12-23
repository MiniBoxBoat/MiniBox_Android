package com.jay86.minibox.utils.extension

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by Jay
 */
fun ImageView.setImageUrl(url: String?, placeHolder: Drawable? = null, errorHolder: Drawable? = null) {
    Glide.with(context)
            .load(url)
            .placeholder(placeHolder)
            .error(errorHolder)
            .into(this)
}