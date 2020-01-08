package com.handtruth.mc.minecraft.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

data class ChatObject(
    val text: String,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underlined: Boolean = false,
    val strikethrough: Boolean = false,
    val obfuscated: Boolean = false,
    val color: Color? = null,
    val insertion: String? = null,
    val clickEvent: ChatEvent? = null,
    val hoverEvent: ChatEvent? = null,
    val extra: List<ChatObject> = emptyList()
) {
    data class ChatEvent(
        val action: String,
        val value: String
    )

    private fun flat(builder: StringBuilder) {
        builder.append(text)
        for (each in extra)
            each.flat(builder)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        flat(builder)
        return builder.toString()
    }

    val length: Int = text.length + extra.sumBy { it.length }

    @Suppress("unused")
    enum class Color(val color: String, val css: String, val code: Int) {
        Unknown("unknown", "white", 0),
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

        @JsonValue
        override fun toString(): String {
            return color
        }

        companion object {
            private val byName = values().associateBy { it.color }
            private val byCSS = values().associateBy { it.css }

            @JsonCreator
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
        val empty = ChatObject("")
    }
}
