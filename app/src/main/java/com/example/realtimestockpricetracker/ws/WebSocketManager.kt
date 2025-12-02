package com.example.realtimestockpricetracker.ws

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.*
import okio.ByteString
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class WebSocketManager {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val retryDelay = 2000L

    fun incomingMessages(): Flow<String> = callbackFlow {

        fun connect() {
            val request = Request.Builder()
                .url("wss://ws.postman-echo.com/raw")
                .build()

            webSocket = client.newWebSocket(request, object : WebSocketListener() {

                override fun onOpen(ws: WebSocket, response: Response) {
                    trySend("::CONNECTED::").isSuccess
                }

                override fun onMessage(ws: WebSocket, text: String) {
                    trySend(text).isSuccess
                }

                override fun onMessage(ws: WebSocket, bytes: ByteString) {
                    trySend(bytes.utf8()).isSuccess
                }

                override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                    trySend("::CLOSING::").isSuccess
                }

                override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                    trySend("::CLOSED::$reason").isSuccess
                    webSocket = null
                }

                override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                    trySend("::ERROR::${t.message}").isSuccess
                    webSocket = null
                }
            })
        }

        while (isActive) {
            if (webSocket == null) {
                connect()
            }
            delay(retryDelay)
        }

        awaitClose {
            webSocket?.close(1000, "Client closed")
            webSocket = null
        }
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    fun stop() {
        webSocket?.close(1000, "stop")
        webSocket = null
    }
}
