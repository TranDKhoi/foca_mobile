package com.example.foca_mobile.socket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException
import java.util.Collections.singletonMap


object SocketHandler {
    private lateinit var mSocket: Socket

    @Synchronized
    fun initSocket(token: Int) {
        try {

            val options = IO.Options.builder()
                .setAuth(singletonMap("token", token.toString()))
                .build()
//            mSocket = IO.socket("https://foca-backend.herokuapp.com", options)
            mSocket = IO.socket("http://10.0.2.2:5000", options)
            Log.d("mSocket.connect()", "RUNNING")
            mSocket.connect()

            mSocket.on(Socket.EVENT_CONNECT, Emitter.Listener {
                Log.d("SOCKET CONNECT: ", "SUCCESSFULLY")
            }
            )
            mSocket.on(Socket.EVENT_CONNECT_ERROR, Emitter.Listener {
                Log.d("SOCKET CONNECT: ", "FAILED! Reconnecting!")
                mSocket.connect()
            })
        } catch (e: URISyntaxException) {
            Log.d("Error init socket: ", e.toString())
        }
    }

    fun getSocket(): Socket {
        return mSocket;
    }
}