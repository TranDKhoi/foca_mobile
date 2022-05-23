package com.example.foca_mobile.activity.user.chat

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foca_mobile.databinding.ActivityChatScreenBinding
import com.example.foca_mobile.model.Message
import com.example.foca_mobile.model.Room
import com.example.foca_mobile.model.User
import com.example.foca_mobile.socket.SocketHandler
import com.example.foca_mobile.utils.GlobalObject
import com.example.foca_mobile.utils.LoginPrefs
import com.google.gson.Gson
import io.socket.client.Ack
import io.socket.client.Socket
import org.json.JSONObject


interface OnKeyboardVisibilityListener {
    fun onVisibilityChanged(visible: Boolean)
}

class UserChatScreen : AppCompatActivity(), OnKeyboardVisibilityListener {
    private lateinit var binding: ActivityChatScreenBinding

    private lateinit var conversationAdapter: ConversationAdapter
    private lateinit var listMessage: ArrayList<Message>
    private lateinit var user: User
    private lateinit var socket: Socket
    private lateinit var room: Room
    private lateinit var partner: User

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityChatScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setKeyboardVisibilityListener(this)

        user = LoginPrefs.getUser()
        binding.messName.text = "Admin"
        binding.messStatus.text = "Online"
        binding.messImage.setImageURI(null)
        GlobalObject.isOpenActivity = true

        //CHANGE SEND BTN VISIBILITY
        binding.inputText.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                binding.sendBtn.visibility = View.GONE
            } else
                binding.sendBtn.visibility = View.VISIBLE
        }

        listMessage = ArrayList()

        conversationAdapter = ConversationAdapter(this, listMessage);
        binding.conversationRCV.adapter = conversationAdapter
        binding.conversationRCV.scrollToPosition(listMessage.size - 1)

        //Socket
        binding.progressBar.visibility = ProgressBar.VISIBLE
        socket = SocketHandler.getSocket()
        socket.emit("get_room_with_admin", Ack {
            val dataJson = it[0] as JSONObject
            val error = Gson().fromJson(dataJson["error"].toString(), String::class.java)
            if (error == null) {
                room = Gson().fromJson(dataJson["data"].toString(), Room::class.java)
                if (room != null) {
                    partner = room.members?.find {
                        it.id != user.id
                    }!!
                    runOnUiThread {
                        binding.messName.text = partner.fullName
                        binding.messStatus.text = "Online"
                        Glide.with(applicationContext).load(partner.photoUrl!!).into(binding.messImage)
                        listMessage.clear()
                        listMessage.addAll(room.messages!!)
                        conversationAdapter.notifyDataSetChanged()
                        binding.conversationRCV.scrollToPosition(listMessage.size - 1)
                        binding.progressBar.visibility = ProgressBar.GONE
                    }
                }
            } else {
                Log.d("Error get_room_with_admin", error)
            }
        })

        socket.on("received_message") {
            val messageJson = it[0] as JSONObject
            val message = Gson().fromJson(messageJson.toString(), Message::class.java)
            Log.d("received_message object", message.toString())
            runOnUiThread {
                listMessage.add(message)
                conversationAdapter.notifyDataSetChanged()
                binding.conversationRCV.scrollToPosition(listMessage.size - 1)
            }
        }
    }

    fun toListMessScreen(view: View) {
        this.finish();
    }

    fun callUserFunc(view: View) {
        val it = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + partner.phoneNumber))
        startActivity(it)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun sendMessageFunc(view: View) {
        if (!binding.inputText.text.isNullOrEmpty()) {
            binding.txtSending.visibility = TextView.VISIBLE
            val message = Message(binding.inputText.text.toString().trim(), user.id, roomId = room.id)
            val messageJson = Gson().toJson(message)
            listMessage.add(message)
            conversationAdapter.notifyDataSetChanged()
            socket.emit("send_message", messageJson, Ack {
                val dataJson = it[0] as JSONObject
                val error = Gson().fromJson(dataJson["error"].toString(), String::class.java)
                if (error == null) {
                    val createdMessage =
                        Gson().fromJson(dataJson["data"].toString(), Message::class.java)
                    runOnUiThread {
                        binding.txtSending.visibility = TextView.GONE
                    }
                } else {
                    Log.d("Error get_room_with_admin", error)
                }
            })
            binding.conversationRCV.scrollToPosition(listMessage.size - 1)
            binding.inputText.text.clear()
            binding.inputText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }
    }

    override fun onVisibilityChanged(visible: Boolean) {
        if (visible)
            if (listMessage.size != 0)
                binding.conversationRCV.smoothScrollToPosition(conversationAdapter.itemCount - 1)
    }

    private fun setKeyboardVisibilityListener(onKeyboardVisibilityListener: OnKeyboardVisibilityListener) {
        val parentView = (findViewById<View>(R.id.content) as ViewGroup).getChildAt(0)
        parentView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            private var alreadyOpen = false
            private val defaultKeyboardHeightDP = 100
            private val EstimatedKeyboardDP =
                defaultKeyboardHeightDP + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48 else 0
            private val rect: Rect = Rect()
            override fun onGlobalLayout() {
                val estimatedKeyboardHeight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    EstimatedKeyboardDP.toFloat(),
                    parentView.resources.displayMetrics
                )
                    .toInt()
                parentView.getWindowVisibleDisplayFrame(rect)
                val heightDiff: Int = parentView.rootView.height - (rect.bottom - rect.top)
                val isShown = heightDiff >= estimatedKeyboardHeight
                onKeyboardVisibilityListener.onVisibilityChanged(isShown)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalObject.isOpenActivity = false
    }
}