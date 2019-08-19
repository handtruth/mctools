package com.handtruth.mc.minecraft.proto

import com.handtruth.mc.paket.Paket

class HandshakePaket(version: Int = -1,
                     address: String = "localhost",
                     port: Int = 25565,
                     state: States = States.Nothing) : Paket() {
    override val id = PaketID.HandshakeRequestResponse
    var version by varInt(version)
    var address by string(address)
    var port by uint16(port)
    var state by enumField(state, States.values())

    enum class States {
        Nothing, Status, Login
    }
}
