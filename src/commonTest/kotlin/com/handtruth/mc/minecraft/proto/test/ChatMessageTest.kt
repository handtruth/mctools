package com.handtruth.mc.minecraft.proto.test

import com.handtruth.mc.minecraft.model.ChatMessage
import com.handtruth.mc.minecraft.util.parseControlSequences
import kotlin.test.Test
import kotlin.test.assertEquals

class ChatMessageTest {

    @Test
    fun message() {
        val expected = ChatMessage(
            extra = listOf(
                ChatMessage(text = "Paradox Universe", color = ChatMessage.Color.DarkPurple, bold = true),
                ChatMessage(text = " "),
                ChatMessage(text = "(", color = ChatMessage.Color.Red),
                ChatMessage(text = "SpaceTech", color = ChatMessage.Color.Gray, italic = true),
                ChatMessage(text = ")", color = ChatMessage.Color.Red),
                ChatMessage(text = " "),
                ChatMessage(text = "QKM", color = ChatMessage.Color.Gold, bold = true, underlined = true)
            )
        )
        val actual = parseControlSequences("§r§5§lParadox Universe§r §c(§7§oSpaceTech§r§c)§r §6§l§nQKM").flatten()
        assertEquals(expected, actual)
    }

}
