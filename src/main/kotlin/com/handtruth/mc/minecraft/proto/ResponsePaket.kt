package com.handtruth.mc.minecraft.proto

import com.handtruth.mc.paket.Paket

class ResponsePaket(message: String = "") : Paket() {
    override val id = PaketID.HandshakeRequestResponse
    var message by string(message)
}
