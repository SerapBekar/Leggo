package com.serapbekar.fprojectsb.common

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context).load(url).into(this)
}