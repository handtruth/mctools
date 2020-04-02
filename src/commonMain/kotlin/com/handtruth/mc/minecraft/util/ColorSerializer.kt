package com.handtruth.mc.minecraft.util

import com.handtruth.mc.minecraft.model.ChatMessage
import kotlinx.serialization.*

@Serializer(forClass = ChatMessage.Color::class)
object ColorSerializer : KSerializer<ChatMessage.Color> {
    override val descriptor = PrimitiveDescriptor(
        "com.handtruth.mc.minecraft.module.ChatObject.Color", PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: ChatMessage.Color) {
        encoder.encodeString(value.color)
    }

    override fun deserialize(decoder: Decoder): ChatMessage.Color {
        return ChatMessage.Color.getByName(decoder.decodeString())
    }
}
