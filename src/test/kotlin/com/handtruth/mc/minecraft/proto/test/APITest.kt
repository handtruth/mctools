package com.handtruth.mc.minecraft.proto.test

import com.handtruth.mc.minecraft.getServerStatus
import org.junit.jupiter.api.Test

class APITest {
    @Test
    fun `Get vanilla status`() {
        val response = getServerStatus("vanilla.mc.handtruth.com", 25565)
        println(response)
    }
}
