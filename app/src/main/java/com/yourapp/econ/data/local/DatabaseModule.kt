package com.yourapp.econ.data.local

import android.content.Context
import com.yourapp.econ.data.local.AppDb
import com.yourapp.econ.data.local.SessionDao
import com.yourapp.econ.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun db(@ApplicationContext ctx: Context): AppDb = AppDb.build(ctx)

    @Provides
    fun userDao(db: AppDb): UserDao = db.userDao()

    @Provides
    fun sessionDao(db: AppDb): SessionDao = db.sessionDao()
}
