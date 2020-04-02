package com.handtruth.mc.minecraft.util

import com.handtruth.mc.minecraft.model.ChatMessage
import kotlinx.serialization.json.*

object ChatMessageSerializer : JsonTransformingSerializer<ChatMessage>(
    ChatMessage.serializer(), "chatVariant"
) {
    override fun readTransform(element: JsonElement): JsonElement {
        return when (element) {
            is JsonArray -> json {
                "extra" to element
            }
            is JsonLiteral, is JsonPrimitive -> json {
                println("Was Here")
                "text" to element
            }
            is JsonObject -> element
            else -> error("unknown element")
        }
    }
}
