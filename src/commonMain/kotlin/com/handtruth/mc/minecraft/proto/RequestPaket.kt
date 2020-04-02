package com.handtruth.mc.minecraft.proto

import com.handtruth.mc.paket.Paket
import com.handtruth.mc.paket.PaketCreator

object RequestPaket : Paket(), PaketCreator<RequestPaket> {
    override val id = PaketID.HandshakeRequestResponse

    override fun produce() = this
}
