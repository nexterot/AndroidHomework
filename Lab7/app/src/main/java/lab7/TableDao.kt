package com.example.koshelev.labs.javalab7

import android.animation.IntArrayEvaluator

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TableDao {
    @get:Query("SELECT * FROM textTable")
    val all: MutableList<TableEntity>

    //@Query("SELECT * FROM textTable WHERE id IN (:tableIds)")
    //List<TableEntity> loadAllByIds(IntArrayEvaluator tableIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRow(tableEntity: TableEntity)

    //@Query("DELETE FROM textTable WHERE id LIKE :id")
    //void deleteById(String id);

    @Query("DELETE FROM textTable WHERE text_column LIKE :text")
    fun deleteByText(text: String)

    @Delete
    fun deleteRow(tableEntity: TableEntity)
}
