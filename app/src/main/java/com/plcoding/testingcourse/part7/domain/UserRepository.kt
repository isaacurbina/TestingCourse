package com.plcoding.testingcourse.part7.domain

interface UserRepository {
    suspend fun getProfile(userId: String): Result<Profile>
}
