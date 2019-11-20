package com.example.koshelev.labs.javalab7

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var mItemsList: RecyclerView? = null
    private lateinit var mListAdapter: ListAdapter
    private lateinit var mDb: TextRoomDatabase
    private var elemsList: MutableList<TableEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        elemsList = ArrayList()

        mItemsList = findViewById<View>(R.id.rv_items) as RecyclerView

        val linearLayoutManager = LinearLayoutManager(this)
        mItemsList!!.layoutManager = linearLayoutManager

        mListAdapter = ListAdapter(elemsList)
        mItemsList!!.adapter = mListAdapter

        mItemsList!!.setHasFixedSize(true)

        // Получаем БД
        mDb = Room.databaseBuilder(
                applicationContext,
                TextRoomDatabase::class.java,
                "app_database"
        ).build()

        val currentActivity = this

        // Получаем данные из БД
        UpdateElemsTask(mDb, mListAdapter, currentActivity).execute()

        // Кнопка для добавления элементов в БД
        val addElemButton = findViewById<View>(R.id.button_add) as Button
        val editTextElem = findViewById<View>(R.id.edit_elem) as EditText
        addElemButton.setOnClickListener {
            // Добавляем в БД элемент из EditText
            val text = editTextElem.text.toString()
            InsertElemTask(text, mDb, mListAdapter, currentActivity).execute()
        }

        ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                    override fun onMove(recyclerView: RecyclerView,
                                        viewHolder: RecyclerView.ViewHolder,
                                        target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val elemText = (viewHolder as ElemViewHolder).data
                        DeleteElemTask(elemText, mDb, mListAdapter, currentActivity).execute()
                    }
                }
        ).attachToRecyclerView(mItemsList)
    }
}
