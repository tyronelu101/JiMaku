package com.example.jimaku.di

import android.content.Context
import androidx.room.Room
import com.example.jimaku.database.JiMakuDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun provideDefineDatabase(@ApplicationContext context: Context): JiMakuDatabase =
        Room.databaseBuilder(
            context, JiMakuDatabase::class.java,
            "jimaku-database",
        ).build()
}