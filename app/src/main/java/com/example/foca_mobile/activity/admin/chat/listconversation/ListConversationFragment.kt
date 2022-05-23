package com.example.foca_mobile.activity.admin.chat.listconversation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.foca_mobile.activity.admin.chat.conversation.AdminChatScreen
import com.example.foca_mobile.databinding.FragmentAdminListConversationBinding
import com.example.foca_mobile.model.Message
import com.example.foca_mobile.socket.SocketHandler
import com.example.foca_mobile.utils.GlobalObject
import com.google.gson.Gson
import io.socket.client.Ack
import io.socket.client.Socket
import org.json.JSONObject


class ListConversationFragment : Fragment() {
    private lateinit var conversationList: ArrayList<Conversation>
    private lateinit var conversationListAdapter: ListConversationAdapter

    private var _binding: FragmentAdminListConversationBinding? = null
    private val binding get() = _binding!!

    private lateinit var socket: Socket
    private var roomId: Int = 0

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminListConversationBinding.inflate(layoutInflater)
        socket = SocketHandler.getSocket()

        conversationList = ArrayList()

        getRooms()
        //SET ADAPTER CHO RCV
        conversationListAdapter = ListConversationAdapter(conversationList)
        binding.messRcV.adapter = conversationListAdapter

        socket.on("received_message") {
            val messageJson = it[0] as JSONObject
            val message = Gson().fromJson(messageJson.toString(), Message::class.java)

            GlobalObject.updateNotSeenConversationAdmin(message.roomId!!)
            roomId = message.roomId
            socket.emit("get_rooms", Ack { ack ->
                val dataJson = ack[0] as JSONObject
                val dataConversation =
                    Gson().fromJson(dataJson.toString(), ConversationListObj::class.java)
                if (dataConversation.error == null) {
                    activity?.runOnUiThread {
                        conversationList.clear()
                        conversationList.addAll(dataConversation.data ?: ArrayList())
                        conversationListAdapter.notifyDataSetChanged()
                    }
                }
            })
        }

        conversationListAdapter.onItemClick = {
            val intent = Intent(activity, AdminChatScreen::class.java)
            intent.putExtra("conversationId", it.id)
            startActivityForResult(intent, 1)
        }
        SocketHandler.getSocket().on("send_message") { args ->
            Log.d("send_message event: ", args[0].toString())
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        getRooms()
//        GlobalObject.updateNotSeenConversationAdmin(roomId, true)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getRooms() {
        binding.progressBar.visibility = ProgressBar.VISIBLE
        socket.emit("get_rooms", Ack {
            val dataJson = it[0] as JSONObject
            val dataConversation =
                Gson().fromJson(dataJson.toString(), ConversationListObj::class.java)
            if (dataConversation.error == null) {
                activity?.runOnUiThread {
                    conversationList.clear()
                    conversationList.addAll(dataConversation.data ?: ArrayList())
                    conversationListAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = ProgressBar.GONE
                }
            }
        })
    }
}