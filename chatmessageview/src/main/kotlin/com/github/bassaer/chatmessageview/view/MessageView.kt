package com.github.bassaer.chatmessageview.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.ListView
import com.github.bassaer.chatmessageview.model.Attribute
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.util.MessageDateComparator
import com.github.bassaer.chatmessageview.util.TimeUtils
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

/**
 * Simple chat view
 * Created by nakayama on 2016/08/08.
 */
class MessageView : ListView, View.OnFocusChangeListener {

    /**
     * All contents such as right message, left message, date label
     */
    private var chatList: MutableList<Any> = ArrayList()

    /**
     * Only messages
     */
    val messageList = ArrayList<Message>()

    private lateinit var messageAdapter: MessageAdapter

    private lateinit var keyboardAppearListener: OnKeyboardAppearListener

    private var customView: View? = null

    /**
     * MessageView is refreshed at this time
     */
    private var refreshInterval: Long = 60000

    private var timeInterval: Long = 15 * 60 * 1000

    private var attribute: Attribute

    interface OnKeyboardAppearListener {
        fun onKeyboardAppeared(hasChanged: Boolean)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        attribute = Attribute(context, attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attribute = Attribute(context, attrs)
        init()
    }


    fun init(list: List<Message>) {
        chatList = ArrayList()
        choiceMode = ListView.CHOICE_MODE_NONE

        for (i in list.indices) {
            addMessage(list[i])
        }
        refresh()
        init()
    }

    fun init(attribute: Attribute) {
        this.attribute = attribute
        init()
    }

    /**
     * Initialize list
     */
    fun init() {
        dividerHeight = 0
        messageAdapter = MessageAdapter(context, 0, chatList, attribute)

        adapter = messageAdapter

        val weakMessageAdapter: WeakReference<MessageAdapter> = WeakReference(messageAdapter)
        val handler = Handler()
        val refreshTimer = Timer(true)
        refreshTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post { weakMessageAdapter.get()?.notifyDataSetChanged() }
            }
        }, 1000, refreshInterval)
    }

    /**
     * Set new message and refresh
     * @param message new message
     */
    fun setMessage(message: Message) {
        addMessage(message)
        refresh()
    }

    /**
     * Dynamically update message status and refresh, updating the status icon
     * @param message message to update
     * @param status new status to be applied
     */
    fun updateMessageStatus(message: Message, status: Int) {
        val indexOfMessage = messageList.indexOf(message)
        val messageToUpdate = messageList[indexOfMessage]
        messageToUpdate.status = status
        messageAdapter.notifyDataSetChanged()
    }

    /**
     * Add message to chat list and message list.
     * Set date text before set message if sent at the different day.
     * @param message new message
     */
    private fun addMessage(message: Message) {
        messageList.add(message)
    }

    private fun refresh() {
        sortMessages(messageList)
        chatList.clear()
        chatList.addAll(insertDateSeparator(messageList))
        messageAdapter.notifyDataSetChanged()
    }

    fun remove(message: Message) {
        messageList.remove(message)
        refresh()
    }

    fun removeAll() {
        messageList.clear()
        refresh()
    }

    private fun insertDateSeparator(list: List<Message>): List<Any> {
        val result = ArrayList<Any>()
        if (list.isEmpty()) {
            return result
        }

        val message = list[0]
        result.add(message.dateSeparateText)

        if (customView != null) {
            result.add(customView!!)
        }

        if (message.type == Message.Type.HEADER) {
            result.add(message.text!!)
        } else {
            result.add(displayIconMessage(message, 1))
        }

        if (list.size < 2) {
            return result
        }

        for (i in 1 until list.size) {
            val prevMessage = list[i - 1]
            val currMessage = list[i]
            if (!TimeUtils.isDisplayTimeInterval(prevMessage.sendTime, currMessage.sendTime, timeInterval)) {
                result.add(currMessage.dateSeparateText)
            }

            if (currMessage.type == Message.Type.HEADER) {
                result.add(currMessage.text!!)
            } else {
                result.add(displayIconMessage(currMessage, i + 1))
            }


        }
        return result
    }

    private fun displayIconMessage(message: Message, position: Int): Message {
        if (position < messageList.size) {
            val nextMessage = messageList[position]
            if (nextMessage.type == Message.Type.HEADER) {
                displayIconMessage(message, position + 1)
            } else {
                if (nextMessage.user.getId() == message.user.getId()) {
                    message.iconVisibility = nextMessage.user.getId() != message.user.getId()
                }
            }
        }
        return message
    }

    /**
     * Sort messages
     */
    private fun sortMessages(list: List<Message>?) {
        if (list != null) {
            val dateComparator = MessageDateComparator()
            Collections.sort(list, dateComparator)
        }
    }

    fun setOnKeyboardAppearListener(listener: OnKeyboardAppearListener) {
        keyboardAppearListener = listener
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        // if ListView became smaller
        if (::keyboardAppearListener.isInitialized && height < oldHeight) {
            keyboardAppearListener.onKeyboardAppeared(true)
        }
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus) {
            //Scroll to end
            scrollToEnd()
        }
    }

    fun scrollToEnd() {
        smoothScrollToPosition(count - 1)
    }

    fun setLeftBubbleColor(color: Int) {
        messageAdapter.setLeftBubbleColor(color)
    }

    fun setRightBubbleColor(color: Int) {
        messageAdapter.setRightBubbleColor(color)
    }

    fun setLeftBgDrawable(drawable: Drawable) {
        messageAdapter.setLeftBgDrawable(drawable)
    }

    fun setRightBgDrawable(drawable: Drawable) {
        messageAdapter.setRightBgDrawable(drawable)
    }

    fun setUsernameTextColor(color: Int) {
        messageAdapter.setUsernameTextColor(color)
    }

    fun setSendTimeTextColor(color: Int) {
        messageAdapter.setSendTimeTextColor(color)
    }

    fun setMessageStatusColor(color: Int) {
        messageAdapter.setStatusColor(color)
    }

    fun setDateSeparatorTextColor(color: Int) {
        messageAdapter.setDateSeparatorColor(color)
    }

    fun setRightMessageTextColor(color: Int) {
        messageAdapter.setRightMessageTextColor(color)
    }

    fun setLeftMessageTextColor(color: Int) {
        messageAdapter.setLeftMessageTextColor(color)
    }

    fun setOnBubbleClickListener(listener: Message.OnBubbleClickListener) {
        messageAdapter.setOnBubbleClickListener(listener)
    }

    fun setOnBubbleLongClickListener(listener: Message.OnBubbleLongClickListener) {
        messageAdapter.setOnBubbleLongClickListener(listener)
    }

    fun setOnIconClickListener(listener: Message.OnIconClickListener) {
        messageAdapter.setOnIconClickListener(listener)
    }

    fun setOnIconLongClickListener(listener: Message.OnIconLongClickListener) {
        messageAdapter.setOnIconLongClickListener(listener)
    }

    fun setMessageMarginTop(px: Int) {
        messageAdapter.setMessageTopMargin(px)
    }

    fun setMessageMarginBottom(px: Int) {
        messageAdapter.setMessageBottomMargin(px)
    }

    fun setRefreshInterval(refreshInterval: Long) {
        this.refreshInterval = refreshInterval
    }

    fun setTimeInterval(timeInterval: Long) {
        this.timeInterval = timeInterval
    }

    fun setMessageFontSize(size: Float) {
        attribute.messageFontSize = size
        setAttribute()
    }

    fun setUsernameFontSize(size: Float) {
        attribute.usernameFontSize = size
        setAttribute()
    }

    fun setTimeLabelFontSize(size: Float) {
        attribute.timeLabelFontSize = size
        setAttribute()
    }

    fun setMessageMaxWidth(width: Int) {
        attribute.messageMaxWidth = width
        setAttribute()
    }

    fun setDateSeparatorFontSize(size: Float) {
        attribute.dateSeparatorFontSize = size
        setAttribute()
    }

    fun addCustomView(view: View) {
        this.customView = view
        refresh()
    }

    fun getCustomView(): View? = customView

    private fun setAttribute() {
        messageAdapter.setAttribute(attribute)
    }
}
