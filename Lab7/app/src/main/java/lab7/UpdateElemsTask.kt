package com.example.koshelev.labs.javalab7

import android.os.AsyncTask

import java.net.URL

class UpdateElemsTask(private val database: TextRoomDatabase, private val adapter: ListAdapter, private val activity: MainActivity) : AsyncTask<URL, Void, MutableList<TableEntity>>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg urls: URL): MutableList<TableEntity> {
        return database.tableDao().all
    }

    override fun onPostExecute(list: MutableList<TableEntity>) {
        activity.runOnUiThread { adapter.setData(list) }
        super.onPostExecute(list)
    }
}
