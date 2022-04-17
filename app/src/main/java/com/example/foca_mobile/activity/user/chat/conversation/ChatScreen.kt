package com.example.foca_mobile.activity.user.chat.conversation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.chat.listmess.ListMessageClass
import kotlinx.android.synthetic.main.activity_chat_screen.*

class ChatScreen : AppCompatActivity() {

    private lateinit var receiveObject: ListMessageClass

    private lateinit var conversationAdapter: ConversationAdapter
    private lateinit var conversation: ArrayList<ConversationClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_chat_screen)

        receiveObject = intent.getParcelableExtra<ListMessageClass>("mess")!!;
        if (receiveObject != null) {
            messName.text = receiveObject.name
            messStatus.text = "Online"
            messImage.setImageResource(receiveObject.image)
        }

        //CHANGE SEND BTN VISIBILITY
        inputText.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                sendBtn.visibility = View.GONE
            } else
                sendBtn.visibility = View.VISIBLE
        }


        //SET ADAPTER
        conversation = ArrayList()
        conversation.add(ConversationClass("Tôi nhận", "1"))
        conversation.add(ConversationClass("Tôi nhận", "1"))
        conversation.add(ConversationClass("Tôi gửi", "2"))
        conversation.add(ConversationClass("Tôi nhận", "1"))
        conversation.add(ConversationClass("Tôi gửi", "2"))
        conversation.add(ConversationClass("Tôi nhận", "1"))
        conversation.add(ConversationClass("Tôi nhận", "1"))
        conversation.add(ConversationClass("Tôi nhận", "1"))
        conversation.add(ConversationClass("Tôi gửi", "2"))
        conversation.add(ConversationClass("Tôi nhận", "1"))
        conversation.add(ConversationClass("Tôi gửi", "2"))
        conversation.add(ConversationClass("Tôi nhận", "1"))
        conversation.add(ConversationClass("Tôi nhận", "1"))
        conversation.add(ConversationClass("Tôi gửi", "2"))
        conversation.add(ConversationClass("Tôi nhận", "1"))
        conversation.add(ConversationClass("Tôi nhận", "1"))
        conversation.add(ConversationClass("Tôi gửi", "2"))
        conversation.add(ConversationClass("Tôi nhận", "1"))

        conversationAdapter = ConversationAdapter(this, conversation);
        conversationRCV.layoutManager = LinearLayoutManager(this)
        conversationRCV.adapter = conversationAdapter
        conversationRCV.scrollToPosition(conversation.size - 1)

    }

    fun toListMessScreen(view: View) {
        this.finish();
    }

    fun callUserFunc(view: View) {

        val it = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "0968717777"))
        startActivity(it)
    }

    fun sendMessageFunc(view: View) {
        if (!inputText.text.isNullOrEmpty()) {
            conversation.add(ConversationClass(inputText.text.toString(), "2"));
            conversationRCV.scrollToPosition(conversation.size - 1)
            inputText.text.clear();
            inputText.onEditorAction(EditorInfo.IME_ACTION_DONE);
        }

    }
}