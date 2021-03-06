package com.github.bassaer.chatmessageview.util

import java.util.*

/**
 * Date formatter of chat timeline separator.
 * Created by nakayama on 2017/01/13.
 */
class DateFormatter : ITimeFormatter {
    override fun getFormattedTimeText(createdAt: Calendar): String {
        if (TimeUtils.isSameDay(createdAt, Calendar.getInstance())) {
            return "Today ${TimeUtils.calendarToString(createdAt, "hh:mm a")}"
        } else {
            return TimeUtils.calendarToString(createdAt, "MMM dd, yyyy hh:mm a")
        }
    }
}
