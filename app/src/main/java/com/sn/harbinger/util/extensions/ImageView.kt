package com.sn.harbinger.util.extensions

import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sn.harbinger.R

fun ImageView.loadImageWithGlideBase64(base64: String) =
    Glide.with(this.context)
        .asBitmap()
        .placeholder(R.drawable.ic_person)
        .error(R.drawable.ic_person)
        .load(if (base64.isEmpty()) R.drawable.ic_person else Base64.decode(base64, Base64.DEFAULT))
        .apply(
            RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .fitCenter()
        )
        .into(this)