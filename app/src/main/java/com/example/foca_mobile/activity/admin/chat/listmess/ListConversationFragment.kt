package com.example.foca_mobile.activity.admin.chat.listmess

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.admin.chat.conversation.AdminChatScreen
import com.example.foca_mobile.databinding.FragmentAdminListConversationBinding
import com.example.foca_mobile.socket.SocketHandler
import com.google.gson.Gson
import io.socket.client.Ack
import org.json.JSONObject


class ListConversationFragment : Fragment() {
    private lateinit var messRecyclerView: RecyclerView
    private lateinit var conversationList: ArrayList<Conversation>
    private lateinit var conversationListAdapter: ListConversationAdapter

    private lateinit var binding: FragmentAdminListConversationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_admin_list_conversation, container, false)
        binding = FragmentAdminListConversationBinding.bind(view)

        //ÁNH XẠ RCV VÀ  SET LAYOUT CHO NÓ
        messRecyclerView = view.findViewById(R.id.messRcV)
        messRecyclerView.layoutManager = LinearLayoutManager(activity)
        messRecyclerView.setHasFixedSize(true)

        conversationList = ArrayList()

        getRooms()

        //SET ADAPTER CHO RCV
        conversationListAdapter = ListConversationAdapter(conversationList)
        messRecyclerView.adapter = conversationListAdapter

        conversationListAdapter.onItemClick = {
            val intent = Intent(activity, AdminChatScreen::class.java)
            intent.putExtra("conversation", Gson().toJson(it))
            startActivityForResult(intent, 1)
        }
        SocketHandler.getSocket().on("send_message") { args ->
            Log.d("send_message event: ", args[0].toString())
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        getRooms()
    }

    fun getRooms() {
        binding.progressBar.visibility = ProgressBar.VISIBLE
        val socket = SocketHandler.getSocket()
        socket.emit("get_rooms", Ack {
            val dataJson = it[0] as JSONObject
            val dataConversation =
                Gson().fromJson(dataJson.toString(), ConversationListObj::class.java)
            if (dataConversation.error == null) {
                activity?.runOnUiThread(Runnable {
                    conversationList.clear()
                    conversationList.addAll(dataConversation.data ?: ArrayList())
                    conversationListAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = ProgressBar.GONE
                })
            }
        })

    }
}