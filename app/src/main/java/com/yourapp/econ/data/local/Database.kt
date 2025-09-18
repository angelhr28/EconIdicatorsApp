package com.yourapp.econ.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, SessionEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao

    companion object {
        fun build(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "econ.db").build()
    }
}
