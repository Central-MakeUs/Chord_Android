package com.team.chord.core.network.dto.user

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class DeleteUserRequestDtoTest {
    @Test
    fun `delete user request encodes social access token for unlink`() {
        val json = Json.encodeToString(DeleteUserRequestDto(accessToken = "provider-token"))

        assertEquals("{\"accessToken\":\"provider-token\"}", json)
    }

    @Test
    fun `delete user request can omit nullable token for providers that do not require it`() {
        val json = Json.encodeToString(DeleteUserRequestDto(accessToken = null))

        assertEquals("{}", json)
    }
}
