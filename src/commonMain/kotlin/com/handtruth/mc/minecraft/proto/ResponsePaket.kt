package com.handtruth.mc.minecraft.proto

import com.handtruth.mc.minecraft.model.ServerStatus
import com.handtruth.mc.paket.Paket
import com.handtruth.mc.paket.PaketCreator
import com.handtruth.mc.paket.fields.string
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class ResponsePaket(message: ServerStatus) : Paket() {
    override val id = PaketID.HandshakeRequestResponse
    var message by string(json, message)

    companion object : PaketCreator<ResponsePaket> {
        override fun produce() = ResponsePaket(
            ServerStatus(
                ServerStatus.Version("", -1),
                ServerStatus.Players(0, 0, null)
            )
        )

        private val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
    }
}
