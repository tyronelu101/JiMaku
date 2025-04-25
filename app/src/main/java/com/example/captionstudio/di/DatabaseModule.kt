package com.example.captionstudio.di

import android.content.Context
import androidx.room.Room
import com.example.captionstudio.database.CaptionStudioDatabase
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
    fun provideDefineDatabase(@ApplicationContext context: Context): CaptionStudioDatabase =
        Room.databaseBuilder(
            context, CaptionStudioDatabase::class.java,
            "captionstudio-database",
        ).build()
}