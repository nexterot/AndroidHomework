package com.example.lab7

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TableEntity::class], version = 3, exportSchema = false)
abstract class TextRoomDatabase : RoomDatabase() {
    abstract fun tableDao(): TableDao
}
