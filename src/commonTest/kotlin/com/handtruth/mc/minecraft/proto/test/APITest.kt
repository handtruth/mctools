package com.handtruth.mc.minecraft.proto.test

import com.handtruth.mc.minecraft.MinecraftClient
import com.handtruth.mc.minecraft.UUID
import com.handtruth.mc.minecraft.model.ChatMessage
import com.handtruth.mc.minecraft.model.ServerStatus
import com.handtruth.mc.minecraft.util.buildChat
import io.ktor.test.dispatcher.testSuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.seconds

class APITest {
    @Test
    fun getVanillaStatus() = testSuspend {
        withTimeout(3.seconds) {
            val client = MinecraftClient("vanilla.mc.handtruth.com", 25565)
            println(client.getStatus())
        }
    }

    companion object {
        private val exampleStatus = ServerStatus(
            version = ServerStatus.Version(
                name = "Server",
                protocol = -1
            ),
            players = ServerStatus.Players(
                max = 250, online = 19,
                sample = listOf(
                    ServerStatus.Players.Player(
                        id = UUID("c683771d-8246-4e14-9db0-528b63c265cb"),
                        name = "Popka"
                    ),
                    ServerStatus.Players.Player(
                        id = UUID("80d86712-45be-445e-b16c-5861eacd9624"),
                        name = "Zopka"
                    ),
                    ServerStatus.Players.Player(
                        id = UUID("12b53aaf-4a1c-48d9-a3f3-3b40e220c541"),
                        name = "Player"
                    )
                )
            ),
            description = buildChat {
                text(" ")
                text("Ассоци")
                italic {
                    text("иции ")
                    bold {
                        text("с ")
                        color(ChatMessage.Color.Gold) {
                            underlined {
                                text("лет")
                            }
                            text("а")
                            color(ChatMessage.Color.DarkRed) {
                                text("ю")
                            }
                            italic(false) {
                                text("щ")
                            }
                            text("им")
                        }
                    }
                }
                text(" ")
                strikethrough {
                    text("квадра")
                }
                text("коп")
                obfuscated {
                    text("тером")
                }
            }
        )
    }

    @ExperimentalCoroutinesApi @Test
    fun getExampleStatus() = testSuspend {
        val client = MinecraftClient("example.mc.handtruth.com", 25565)
        val status = client.getStatus()
        assertNotNull(status.favicon)
        val expected = exampleStatus.copy(favicon = status.favicon)
        assertEquals(expected.description, status.description)
        assertEquals(expected, status)
        client.ping().take(3).collect()
    }

    @ExperimentalCoroutinesApi @Test
    fun pingVanilla() = testSuspend {
        val client = MinecraftClient("vanilla.mc.handtruth.com", 25565)
        val ping = client.ping()
            .drop(1)
            .take(10)
            .withIndex()
            .map { (i, ping) -> println("#$i -> $ping"); ping.inMilliseconds }
            .reduce { a, b -> a + b } / 10
        println("${ping}ms")
    }
}
