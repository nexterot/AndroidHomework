package com.example.lab9;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TableDao {
    @Query("SELECT * FROM textTable")
    List<TableEntity> getAll();

    @Query("SELECT * FROM textTable")
    Cursor selectAllCursor();

    @Query("SELECT * FROM textTable WHERE id LIKE :id")
    Cursor selectByIdCursor(String id);

    //@Query("SELECT * FROM textTable WHERE id IN (:tableIds)")
    //List<TableEntity> loadAllByIds(IntArrayEvaluator tableIds);

    // Возвращает ID вставленной записи
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRow(TableEntity tableEntity);

    // Возвращает количество удалённых записей
    @Query("DELETE FROM textTable WHERE id LIKE :id")
    int deleteById(String id);

    @Query("DELETE FROM textTable WHERE text_column LIKE :text")
    void deleteByText(String text);

    @Delete
    void deleteRow(TableEntity tableEntity);
}
