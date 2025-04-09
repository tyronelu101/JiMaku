package com.example.jimaku

import com.example.jimaku.player.AndroidAudioPlayer
import com.example.jimaku.player.AudioPlayer
import com.example.jimaku.recorder.AndroidAudioRecorder
import com.example.jimaku.recorder.AudioRecorder
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