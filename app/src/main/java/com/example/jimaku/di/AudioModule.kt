package com.example.jimaku.di

import com.example.jimaku.domain.player.AndroidAudioPlayer
import com.example.jimaku.domain.player.AudioPlayer
import com.example.jimaku.domain.recorder.AndroidAudioRecorder
import com.example.jimaku.domain.recorder.AudioRecorder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class AudioModule {

    @Binds
    abstract fun bindAudioRecorder(audioRecorder: AndroidAudioRecorder): AudioRecorder

    @Binds
    abstract fun bindAudioPlayer(audioPlayer: AndroidAudioPlayer): AudioPlayer

}