package com.example.captionstudio.di

import com.example.captionstudio.domain.player.AndroidAudioPlayer
import com.example.captionstudio.domain.player.AudioPlayer
import com.example.captionstudio.domain.recorder.AndroidAudioRecorder
import com.example.captionstudio.domain.recorder.AudioRecorder
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