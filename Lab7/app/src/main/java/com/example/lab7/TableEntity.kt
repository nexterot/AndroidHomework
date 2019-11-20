package com.example.lab7

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "textTable")
class TableEntity(@field:PrimaryKey
                  @field:ColumnInfo(name = "text_column")
                  var text: String)
