package com.example.lab7

import android.os.AsyncTask

import java.net.URL

class DeleteElemTask(private val elemText: String, private val database: TextRoomDatabase, private val adapter: ListAdapter,
                     private val activity: MainActivity) : AsyncTask<URL, Void, MutableList<TableEntity>>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg urls: URL): MutableList<TableEntity> {
        database.tableDao().deleteByText(elemText)
        return database.tableDao().all
    }

    override fun onPostExecute(list: MutableList<TableEntity>) {
        activity.runOnUiThread { adapter.setData(list) }
        super.onPostExecute(list)
    }
}
