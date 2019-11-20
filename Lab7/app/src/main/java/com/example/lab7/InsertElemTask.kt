package com.example.lab7

import android.app.Activity
import android.net.Uri
import android.os.AsyncTask
import android.widget.TextView

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.Scanner

class InsertElemTask(private val elemText: String, private val database: TextRoomDatabase, private val adapter: ListAdapter,
                     private val activity: MainActivity) : AsyncTask<URL, Void, MutableList<TableEntity>>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg urls: URL): MutableList<TableEntity> {
        database.tableDao().insertRow(TableEntity(elemText))
        return database.tableDao().all
    }

    override fun onPostExecute(list: MutableList<TableEntity>) {
        activity.runOnUiThread { adapter.setData(list) }
        super.onPostExecute(list)
    }
}
