package com.deyvieat.shoptogether.features.rooms.data.websocket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuctionSocketManager @Inject constructor() {

    private var socket: Socket? = null

    fun connect(
        roomId: String,
        userId: String,
        onMessage: (type: String, data: String) -> Unit
    ) {
        try {
            val options = IO.Options()
            options.query = "roomId=$roomId&userId=$userId"

            // 🔥 IMPORTANTE → Puerto 4000
            socket = IO.socket("http://zuri.space:4000", options)

            socket?.on(Socket.EVENT_CONNECT) {
                Log.d("SOCKET", "✅ Conectado al WebSocket 4000")
            }

            socket?.on("new_vote") { args ->
                val data = args[0].toString()
                Log.d("SOCKET", "📩 new_vote recibido: $data")
                onMessage("new_vote", data)
            }

            socket?.connect()

        } catch (e: Exception) {
            Log.e("SOCKET", "Error conexión: ${e.message}")
        }
    }

    fun sendVote(roomId: String, productId: String, userId: String, value: Double) {
        val json = JSONObject().apply {
            put("roomId", roomId)
            put("productId", productId)
            put("userId", userId)
            put("value", value)
        }

        socket?.emit("place_vote", json)
    }

    fun disconnect() {
        socket?.disconnect()
        socket?.off()
    }
}