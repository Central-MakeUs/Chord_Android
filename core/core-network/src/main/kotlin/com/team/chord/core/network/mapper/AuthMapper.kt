package com.team.chord.core.network.mapper

import com.team.chord.core.domain.model.AuthToken
import com.team.chord.core.network.dto.auth.LoginResponse

fun LoginResponse.toAuthToken(): AuthToken =
    AuthToken(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
