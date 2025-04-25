package com.example.captionstudio.models

import com.example.captionstudio.database.entities.CaptionEntity

data class Caption(
    val timeStampStart: Long,
    val timeStampEnd: Long,
    val text: String
)

fun CaptionEntity.toModel(): Caption = Caption(
    timeStampStart = this.timeStampStart,
    timeStampEnd = this.timeStampEnd,
    text = this.text
)