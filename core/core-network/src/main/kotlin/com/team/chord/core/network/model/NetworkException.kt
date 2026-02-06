package com.team.chord.core.network.model

class ApiException(
    val code: String,
    override val message: String,
    val errors: Map<String, String>? = null,
) : Exception(message)

class UnauthorizedException(
    override val message: String = "인증이 필요합니다",
) : Exception(message)
