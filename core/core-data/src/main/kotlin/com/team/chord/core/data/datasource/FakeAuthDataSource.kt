package com.team.chord.core.data.datasource

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthDataSource @Inject constructor() : AuthDataSource {

    private var storedLoginId: String? = null
    private var storedPassword: String? = null

    override suspend fun signUp(loginId: String, password: String) {
        if (storedLoginId == loginId) {
            throw Exception("이미 사용 중인 아이디입니다")
        }
        storedLoginId = loginId
        storedPassword = password
    }

    override suspend fun login(loginId: String, password: String): LoginResult {
        if (storedLoginId != loginId || storedPassword != password) {
            throw Exception("아이디 또는 비밀번호가 올바르지 않습니다")
        }
        return LoginResult(
            accessToken = UUID.randomUUID().toString(),
            refreshToken = UUID.randomUUID().toString(),
            onboardingCompleted = false,
        )
    }

    override suspend fun refreshToken(refreshToken: String): String {
        return UUID.randomUUID().toString()
    }
}
