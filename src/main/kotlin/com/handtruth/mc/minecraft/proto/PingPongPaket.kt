package com.handtruth.mc.minecraft.proto

import com.handtruth.mc.paket.Paket

class PingPongPaket(payload: Long = 0) : Paket() {
    override val id = PaketID.PingPong
    var payload by int64(payload)
}
