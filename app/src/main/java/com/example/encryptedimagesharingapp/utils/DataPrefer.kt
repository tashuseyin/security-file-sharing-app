package com.example.encryptedimagesharingapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.SHOPAPP_PREFERENCES)

class DataPrefer(private val context: Context) {
    private val data = stringPreferencesKey(Constants.LOGGED_IN_USERNAME)

    val dataFlow: Flow<String>
        get() = context.dataStore.data.map {
            it[data] ?: ""
        }

    suspend fun saveData(name: String) {
        context.dataStore.edit {
            it[data] = name
        }
    }


}