package com.handtruth.mc.minecraft.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.handtruth.mc.minecraft.util.ChatObjectDeserializer
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class ResponseObject(
    val version: Version,
    val players: Players,
    val favicon: String? = null,
    @JsonDeserialize(using = ChatObjectDeserializer::class)
    val description: ChatObject = ChatObject.empty
) {
    data class Version(
        val name: String,
        val protocol: Int
    )
    data class Players(
        val max: Int,
        val online: Int,
        val sample: List<Player>?
    ) {
        data class Player(
            val name: String,
            val id: UUID
        )
    }
}
