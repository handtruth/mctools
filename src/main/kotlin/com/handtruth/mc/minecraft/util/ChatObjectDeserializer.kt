package com.handtruth.mc.minecraft.util

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.handtruth.mc.minecraft.model.ChatObject
import java.lang.IllegalStateException

class ChatObjectDeserializer : JsonDeserializer<ChatObject>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ChatObject {
        return when (p.currentToken) {
            JsonToken.VALUE_STRING -> deserializeString(p.text)
            JsonToken.START_OBJECT -> deserializeObject(p)
            JsonToken.START_ARRAY -> {
                val list = mutableListOf<ChatObject>()
                readList@while (true) list += when (p.nextToken()) {
                    JsonToken.VALUE_STRING -> deserializeString(p.text)
                    JsonToken.START_OBJECT -> deserializeObject(p)
                    JsonToken.END_ARRAY -> break@readList
                    else -> throw IllegalStateException("Wrong Chat Object format")
                }
                if (list.isEmpty()) {
                    ChatObject.empty
                } else {
                    val first = list[0]
                    list.removeAt(0)
                    list.addAll(0, first.extra)
                    ChatObject(first.text, first.bold, first.italic, first.underlined, first.strikethrough,
                        first.obfuscated, first.color, first.insertion, first.clickEvent, first.hoverEvent, list)
                }
            }
            else -> throw IllegalStateException("Wrong Chat Object format")
        }
    }

    private fun deserializeObject(p: JsonParser): ChatObject {
        val chat = p.readValueAs(ChatObject::class.java)
        return if (chat.text.contains('§'))
            deserializeString(chat.text).copy(
                bold = chat.bold,
                italic = chat.italic,
                underlined = chat.underlined,
                strikethrough = chat.strikethrough,
                obfuscated = chat.obfuscated,
                color = chat.color,
                insertion = chat.insertion,
                clickEvent = chat.clickEvent,
                hoverEvent = chat.hoverEvent
            )
        else
            chat
    }

    private fun deserializeString(value: String): ChatObject {
        val list = mutableListOf<ChatObject>()

        var bold = false
        var italic = false
        var underlined = false
        var strikethrough = false
        var obfuscated = false
        var color: ChatObject.Color? = null

        operator fun String.unaryPlus() {
            if (this.isNotEmpty())
                list.add(ChatObject(this, bold, italic, underlined, strikethrough, obfuscated, color))
        }

        var begin = 0
        var end: Int
        while (true) {
            end = value.indexOf('§', begin)
            if (end < 0) {
                +value.substring(begin)
                break
            }
            +value.substring(begin, end)
            if (value.length <= end)
                break
            when (val c = value[end + 1]) {
                // Colors section
                in '0'..'9', in 'a'..'f', in 'A'..'F' -> ChatObject.Color.getByCode(c)
                // Format section
                'k' -> obfuscated = true
                'l' -> bold = true
                'm' -> strikethrough = true
                'n' -> underlined = true
                'o' -> italic = true
                'r' -> {
                    bold = false
                    italic = false
                    underlined = false
                    strikethrough = false
                    obfuscated = false
                    color = null
                }
            }
            begin = end + 2
        }
        return when {
            list.isEmpty() -> ChatObject.empty
            list.size == 1 -> list[0]
            else -> {
                ChatObject("", extra = list)
            }
        }
    }
}
