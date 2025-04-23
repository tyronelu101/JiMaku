package com.example.jimaku.models

import com.example.jimaku.database.entities.CaptionEntity

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