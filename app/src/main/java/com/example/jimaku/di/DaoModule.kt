package com.example.jimaku.di

import com.example.jimaku.database.JiMakuDatabase
import com.example.jimaku.database.TranscriptionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    fun provideDictionaryDao(
        database: JiMakuDatabase
    ): TranscriptionDao = database.transcriptionDao()


}