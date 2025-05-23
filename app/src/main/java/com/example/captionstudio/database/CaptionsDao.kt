package com.example.captionstudio.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.captionstudio.database.entities.CaptionEntity

@Dao
interface CaptionsDao {
    @Insert
    fun insert(transcriptionEntity: CaptionEntity)

    @Update
    fun update(transcriptionEntity: CaptionEntity)

    @Delete
    fun delete(transcriptionEntity: CaptionEntity)
}