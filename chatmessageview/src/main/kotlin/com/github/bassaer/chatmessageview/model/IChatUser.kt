package com.github.bassaer.chatmessageview.model

import android.graphics.Bitmap

interface IChatUser {
    fun getId(): String
    fun getName(): String?
    fun getIcon(): Bitmap?
    fun setIcon(bmp: Bitmap)
    fun getUrl(): String?
    fun getDrawable(): Int?
    fun getTextColor(): Int?
}
