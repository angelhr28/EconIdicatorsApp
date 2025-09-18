package com.yourapp.econ.domain.repository

import com.yourapp.econ.domain.entity.Session
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(email: String, name: String, lastName: String, password: String): Session
    suspend fun login(email: String, password: String): Session
    suspend fun logout()
    fun observeSession(): Flow<Session?>
}
