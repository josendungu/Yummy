package com.example.yummy.domain.use_case.app

import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.net.SocketFactory

object InternetConnection {
    fun execute(socketFactory: SocketFactory):Boolean {
        return try {
            val socket = socketFactory.createSocket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }
}