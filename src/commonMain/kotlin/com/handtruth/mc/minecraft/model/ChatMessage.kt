@file:UseSerializers(ColorSerializer::class, ChatMessageSerializer::class)

package com.handtruth.mc.minecraft.model

import com.handtruth.mc.minecraft.util.ChatMessageSerializer
import com.handtruth.mc.minecraft.util.ColorSerializer
import com.handtruth.mc.minecraft.util.parseControlSequences
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class ChatMessage(
    val text: String = "",
    val bold: Boolean? = null,
    val italic: Boolean? = null,
    val underlined: Boolean? = null,
    val strikethrough: Boolean? = null,
    val obfuscated: Boolean? = null,
    val color: Color? = null,
    val insertion: String? = null,
    val clickEvent: ChatEvent? = null,
    val hoverEvent: ChatEvent? = null,
    val extra: List<ChatMessage> = emptyList()
) {
    @Serializable
    data class ChatEvent(
        val action: String,
        val value: String
    )

    private fun flat(builder: Appendable) {
        builder.append(text)
        for (each in extra)
            each.flat(builder)
    }

    /*
    override fun toString(): String {
        val builder = StringBuilder()
        flat(builder)
        return builder.toString()
    }
    */

    val length: Int = text.length + extra.sumBy { it.length }

    private fun flatten(chats: MutableList<ChatMessage>, extra: List<ChatMessage> = this.extra) {
        for (chat in extra) {
            val newChat = chat.copy(
                bold = chat.bold ?: bold,
                italic = chat.italic ?: italic,
                underlined = chat.underlined ?: underlined,
                strikethrough = chat.strikethrough ?: strikethrough,
                obfuscated = chat.obfuscated ?: obfuscated,
                color = chat.color ?: color,
                insertion = chat.insertion ?: insertion,
                clickEvent = chat.clickEvent ?: clickEvent,
                hoverEvent = chat.hoverEvent ?: hoverEvent,
                extra = emptyList()
            )
            chats += newChat
            newChat.flatten(chats, chat.extra)
        }
    }

    fun flatten(): ChatMessage {
        val chats = mutableListOf<ChatMessage>()
        val root = copy(extra = chats)
        flatten(chats)
        chats.removeAll { it.text == "" }
        return root
    }

    fun resolveControlSequences(): ChatMessage {
        val chat = parseControlSequences(text)
        return copy(text = "", extra = listOf(chat) + extra.map { it.resolveControlSequences() })
    }

    @Suppress("unused")
    enum class Color(val color: String, val css: String, val code: Int) {
        Unknown("unknown", "black", 0),
        Black("black", "black", 0x000000),
        DarkBlue("dark_blue", "darkblue", 0x0000AA),
        DarkGreen("dark_green", "darkgreen", 0x00AA00),
        DarkAqua("dark_aqua", "darkcyan", 0x00AAAA),
        DarkRed("dark_red", "darkred", 0xAA0000),
        DarkPurple("dark_purple", "purple", 0xAA00AA),
        Gold("gold", "gold", 0xFFAA00),
        Gray("gray", "gray", 0xAAAAAA),
        DarkGray("dark_gray", "darkgray", 0x555555),
        Blue("blue", "blue", 0x5555FF),
        Green("green", "green", 0x55FF55),
        Aqua("aqua", "cyan", 0x55FFFF),
        Red("red", "red", 0xFF5555),
        LightPurple("light_purple", "mediumorchid", 0xFF55FF),
        Yellow("yellow", "yellow", 0xFFFF55),
        White("white", "white", 0xFFFFFF);

        override fun toString(): String {
            return color
        }

        companion object {
            private val byName = values().associateBy { it.color }
            private val byCSS = values().associateBy { it.css }

            fun getByName(color: String) = byName[color] ?: Unknown
            fun getByCSS(color: String) = byCSS[color] ?: Unknown
            fun getByCode(code: Char) = when (code) {
                in '0'..'9' -> values()[code - '0' + 1]
                in 'a'..'f' -> values()[code - 'a' + 11]
                in 'A'..'F' -> values()[code - 'A' + 11]
                else -> Unknown
            }
        }
    }

    companion object {
        val empty = ChatMessage("")
    }
}
