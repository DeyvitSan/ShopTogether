package com.deyvieat.shoptogether.features.rooms.data.websocket

import com.deyvieat.shoptogether.core.di.NetworkModule
import com.deyvieat.shoptogether.core.di.WsOkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuctionWebSocketManager @Inject constructor(
    @WsOkHttp private val client: OkHttpClient
) {

    private var webSocket: WebSocket? = null

    fun connect(
        roomId: String,
        onMessage: (String) -> Unit,
        onClosed: () -> Unit = {},
        onFailure: (Throwable) -> Unit = {}
    ) {

        val request = Request.Builder()
            .url("${NetworkModule.WS_BASE_URL}$roomId")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onMessage(webSocket: WebSocket, text: String) {
                onMessage(text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                onClosed()
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: okhttp3.Response?
            ) {
                onFailure(t)
            }
        })
    }

    fun sendBid(amount: Double) {
        webSocket?.send(amount.toString())
    }

    fun disconnect() {
        webSocket?.close(1000, "User left room")
        webSocket = null
    }
}