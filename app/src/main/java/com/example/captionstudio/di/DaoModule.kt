package com.example.captionstudio.di

import com.example.captionstudio.database.CaptionStudioDatabase
import com.example.captionstudio.database.TranscriptionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    fun provideDictionaryDao(
        database: CaptionStudioDatabase
    ): TranscriptionDao = database.transcriptionDao()


}