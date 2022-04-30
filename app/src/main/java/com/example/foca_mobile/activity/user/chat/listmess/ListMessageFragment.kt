package com.example.foca_mobile.activity.user.chat.listmess

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.chat.conversation.ChatScreen
import com.example.foca_mobile.socket.SocketHandler
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ListMessageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var messRecyclerView: RecyclerView
    private lateinit var messArrayList: ArrayList<ListMessageClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_list_message_fragment, container, false)
        //ÁNH XẠ RCV VÀ  SET LAYOUT CHO NÓ
        messRecyclerView = view.findViewById(R.id.messRcV)
        messRecyclerView.layoutManager = LinearLayoutManager(activity)
        messRecyclerView.setHasFixedSize(true)

        //KHỞI TẠO  LIST TIN NHẮN
        messArrayList = arrayListOf()

        val tempMess = ListMessageClass(
            R.drawable.ic_user,
            "Tran khoi",
            "hello khoi oi",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        )
        messArrayList.add(tempMess)
        messArrayList.add(tempMess)
        messArrayList.add(tempMess)
        messArrayList.add(tempMess)


        //SET ADAPTER CHO RCV
        val adapter = ListMessageAdapter(messArrayList)
        messRecyclerView.adapter = adapter

        adapter.onItemClick = {
            val intent = Intent(activity, ChatScreen::class.java)
            intent.putExtra("mess", it)
            startActivity(intent)
            val obj = JSONObject()
            obj.put("message", "Alo ALo")
            SocketHandler.getSocket().emit("join_room", obj)
        }
        SocketHandler.getSocket().on("send_message") { args ->
            Log.d("send_message event: ", args[0].toString())
        }
        // Inflate the layout for this fragment
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListMessageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}