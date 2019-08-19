package com.github.bassaer.chatmessageview.util

class DefaultStatusTextFormatter : IMessageStatusTextFormatter {
    companion object {
        const val STATUS_DELIVERING = 0
        const val STATUS_DELIVERED = 1
        const val STATUS_SEEN = 2
        const val STATUS_ERROR = 3
    }


    override fun getStatusText(status: Int, isRightMessage: Boolean): String {
        when (status) {
            STATUS_DELIVERING -> return "Sending"
            STATUS_DELIVERED -> return "Delivered"
            STATUS_SEEN -> return "Seen"
            STATUS_ERROR -> return "Error"
        }
        return ""
    }

}