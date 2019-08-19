package com.handtruth.mc.minecraft

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.handtruth.mc.minecraft.model.ResponseObject
import com.handtruth.mc.minecraft.proto.HandshakePaket
import com.handtruth.mc.minecraft.proto.RequestPaket
import com.handtruth.mc.minecraft.proto.ResponsePaket
import com.handtruth.mc.paket.PaketTransmitter
import java.net.Socket

private val mapper = ObjectMapper().registerKotlinModule()

fun getServerStatus(host: String, port: Int): ResponseObject {
    Socket(host, port).use { sock ->
        PaketTransmitter.create(sock.getInputStream(), sock.getOutputStream()).use { ts ->
            ts.write(HandshakePaket(address = host, port = port, state = HandshakePaket.States.Status))
            ts.write(RequestPaket)
            val paket: ResponsePaket = ts.read()
            return mapper.readValue(paket.message)
        }
    }
}
