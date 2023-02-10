package com.example.testapp.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.testapp.model.InputData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * yeah, nobody cache JSON in app, it is not a good practise, so this is not how I would do it
 * if I had a choice :) Anyway you specifically asked for it, so I used gson + shared preferences to
 * save Json.
 *
 * How I would do it if it was up to me, after making a retrofit request, and getting Json back,
 * I would simply map json to list of business objects, and cached those in Room DB. Or in this
 * specific app, I would be saving List<InputData> in Room DB, not Json.
 *
 */
class SharedPrefs constructor(context: Context) {

    private val sp: SharedPreferences

    init {
        sp = context.getSharedPreferences("cache", Context.MODE_PRIVATE)
    }

    // not using Dagger/Hilt for such a simple app, so gotta use singleton

    companion object {
        private var instance: SharedPrefs? = null

        fun getInstance(context: Context): SharedPrefs {
            if (instance == null) instance = SharedPrefs(context)

            return instance!!
        }
    }

    private val SAVED_JSON = "SAVED_JSON"

    // ------------------------------------
    // JSON

    fun getValues(): List<InputData> {
        val jsonValues = sp.getString(SAVED_JSON, "")
        val gson = Gson()
        val type = object : TypeToken<List<InputData>>() {}.type

        return gson.fromJson<ArrayList<InputData>>(jsonValues, type)
            ?: return listOf()

    }

    fun saveValues(values: List<InputData>) {
        val gson = Gson()
        sp.edit(commit = true) { putString(SAVED_JSON, gson.toJson(values)) }
    }
}