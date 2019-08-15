package com.github.bassaer.chatmessageview.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

interface IChatUser {
    fun getId(): String
    fun getName(): String?
    fun getIcon(): Bitmap?
    fun setIcon(bmp: Bitmap)
    fun getUrl(): String?
    fun getDrawable(): Drawable?
    fun getTextColor(): Int?
}
