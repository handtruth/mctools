package com.handtruth.mc.minecraft.proto.test

import com.handtruth.mc.minecraft.proto.*
import com.handtruth.mc.paket.PaketTransmitter
import org.junit.jupiter.api.Test
import java.net.Socket
import kotlin.test.assertEquals

class ProtoTest {
    @Test
    fun `Get Status Of Vanilla Server`() {
        val host = "vanilla.mc.handtruth.com"
        val port = 25565
        val sock = Socket(host, port)
        val hs = HandshakePaket(address = host, port = port, state = HandshakePaket.States.Status)
        val ts = PaketTransmitter.create(sock.getInputStream(), sock.getOutputStream())
        println("send: $hs")
        ts.write(hs)
        ts.write(RequestPaket())
        val answer: ResponsePaket = ts.read()
        assertEquals(PaketID.HandshakeRequestResponse, ts.id)
        ts.write(PingPongPaket(System.currentTimeMillis()))
        assertEquals(PaketID.PingPong, ts.catch())
        ts.read<PingPongPaket>()
        println(answer.message)
        ts.close()
        sock.close()
    }
}
