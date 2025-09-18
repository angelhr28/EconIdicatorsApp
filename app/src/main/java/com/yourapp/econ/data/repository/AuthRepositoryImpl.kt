package com.yourapp.econ.data.repository

import at.favre.lib.crypto.bcrypt.BCrypt
import com.yourapp.econ.core.common.AppError
import com.yourapp.econ.data.local.SessionDao
import com.yourapp.econ.data.local.SessionEntity
import com.yourapp.econ.data.local.UserDao
import com.yourapp.econ.data.local.UserEntity
import com.yourapp.econ.domain.entity.Session
import com.yourapp.econ.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sessionDao: SessionDao
) : AuthRepository {

    override fun observeSession(): Flow<Session?> =
        sessionDao.observe()
            .map { it?.let { s -> s.userId?.let { Session(it, s.email!!, s.displayName!!) } } }

    override suspend fun register(
        email: String,
        name: String,
        lastName: String,
        password: String
    ): Session {
        if (email.isBlank() || password.length < 6) throw AppError.Validation("Credenciales inválidas")
        val hash = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        val id = userDao.insert(
            UserEntity(
                email = email,
                name = name,
                lastName = lastName,
                passwordHash = hash
            )
        )
        val session = Session(id, email, "$name $lastName")
        sessionDao.upsert(SessionEntity(0, session.userId, session.email, session.displayName))
        return session
    }

    override suspend fun login(email: String, password: String): Session {
        val user = userDao.findByEmail(email) ?: throw AppError.Auth("Usuario no encontrado")
        val result = BCrypt.verifyer().verify(password.toCharArray(), user.passwordHash)
        if (!result.verified) throw AppError.Auth("Clave inválida")
        val session = Session(user.id, user.email, "${user.name} ${user.lastName}")
        sessionDao.upsert(SessionEntity(0, session.userId, session.email, session.displayName))
        return session
    }

    override suspend fun logout() {
        sessionDao.clear()
    }
}
