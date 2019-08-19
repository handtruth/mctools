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

    @Suppress("unused")
    enum class Color(val color: String, val css: String) {
        Unknown("unknown", "white"),
        Black("black", "black"),
        DarkBlue("dark_blue", "darkblue"),
        DarkGreen("dark_green", "darkgreen"),
        DarkAqua("dark_aqua", "darkcyan"),
        DarkRed("dark_red", "darkred"),
        DarkPurple("dark_purple", "purple"),
        Gold("gold", "gold"),
        Gray("gray", "gray"),
        DarkGray("dark_gray", "darkgray"),
        Blue("blue", "blue"),
        Green("green", "green"),
        Aqua("aqua", "cyan"),
        Red("red", "red"),
        LightPurple("light_purple", "mediumorchid"),
        Yellow("yellow", "yellow"),
        White("white", "white");

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
