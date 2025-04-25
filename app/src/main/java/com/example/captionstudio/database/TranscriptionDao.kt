package com.example.captionstudio.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.captionstudio.database.entities.TranscriptionEntity

@Dao
interface TranscriptionDao {

    @Insert
    fun insert(transcriptionEntity: TranscriptionEntity)

    @Update
    fun update(transcriptionEntity: TranscriptionEntity)

    @Delete
    fun delete(transcriptionEntity: TranscriptionEntity)

}