package com.yourapp.econ.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val name: String,
    val lastName: String,
    val passwordHash: String
)

@Entity(tableName = "session", primaryKeys = ["id"])
data class SessionEntity(
    val id: Int = 0,
    val userId: Long?,
    val email: String?,
    val displayName: String?
)

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?
}

@Dao
interface SessionDao {
    @Query("SELECT * FROM session WHERE id = 0")
    fun observe(): kotlinx.coroutines.flow.Flow<SessionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(session: SessionEntity)

    @Query("DELETE FROM session")
    suspend fun clear()
}
