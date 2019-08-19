package com.handtruth.mc.minecraft.proto

import com.handtruth.mc.paket.Paket

open class RequestPaket : Paket() {
    override val id = PaketID.HandshakeRequestResponse
    companion object : RequestPaket()
}
