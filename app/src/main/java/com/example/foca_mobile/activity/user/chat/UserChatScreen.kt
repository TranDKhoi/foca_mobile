package com.example.foca_mobile.activity.user.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foca_mobile.databinding.ActivityChatScreenBinding
import com.example.foca_mobile.model.Message
import com.example.foca_mobile.model.Room
import com.example.foca_mobile.model.User
import com.example.foca_mobile.socket.SocketHandler
import com.example.foca_mobile.utils.LoginPrefs
import com.google.gson.Gson
import io.socket.client.Ack
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_chat_screen.*
import org.json.JSONObject

class UserChatScreen : AppCompatActivity() {
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

        user = LoginPrefs.getUser()
        messName.text = "Admin"
        messStatus.text = "Online"
        messImage.setImageURI(null)

        //CHANGE SEND BTN VISIBILITY
        inputText.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                sendBtn.visibility = View.GONE
            } else
                sendBtn.visibility = View.VISIBLE
        }

        listMessage = ArrayList()

        conversationAdapter = ConversationAdapter(this, listMessage);
        binding.conversationRCV.layoutManager = LinearLayoutManager(this)
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
                        messName.text = partner.fullName
                        messStatus.text = "Online"
                        Glide.with(applicationContext).load(partner.photoUrl!!).into(messImage)
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
        if (!inputText.text.isNullOrEmpty()) {

            val message = Message(inputText.text.toString().trim(), user.id, roomId = room.id)
            val messageJson = Gson().toJson(message)
            listMessage.add(message)
            conversationAdapter.notifyDataSetChanged()
            socket.emit("send_message", messageJson, Ack {
                val dataJson = it[0] as JSONObject
                val error = Gson().fromJson(dataJson["error"].toString(), String::class.java)
                if (error == null) {
                    val createdMessage =
                        Gson().fromJson(dataJson["data"].toString(), Message::class.java)
                } else {
                    Log.d("Error get_room_with_admin", error)
                }
            })
            conversationRCV.scrollToPosition(listMessage.size - 1)
            binding.inputText.text.clear()
            binding.inputText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }
    }
}