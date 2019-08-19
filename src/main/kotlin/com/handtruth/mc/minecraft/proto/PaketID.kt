package com.handtruth.mc.minecraft.proto

import com.handtruth.mc.paket.PaketTransmitter

enum class PaketID {
    HandshakeRequestResponse, // У Minecraft посложнение обстоят дела с ID пакетов
    PingPong
}

fun PaketTransmitter.catch(): PaketID {
    val id = catchOrdinal()
    return PaketID.values()[id]
}

suspend fun PaketTransmitter.catchAsync(): PaketID {
    val id = catchOrdinalAsync()
    return PaketID.values()[id]
}

val PaketTransmitter.id get() = PaketID.values()[idOrdinal]
