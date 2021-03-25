package com.example.washweather.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class NoteEntity(
        @PrimaryKey(autoGenerate = false)
        var id: Int = 0,
        var text: String = "",
)
