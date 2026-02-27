package com.deyvieat.shoptogether.core.session


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val KEY_TOKEN   = stringPreferencesKey("token")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_NAME    = stringPreferencesKey("user_name")
    }

    val token: Flow<String?> =
        dataStore.data.map { it[KEY_TOKEN] }

    suspend fun saveSession(token: String, userId: String, name: String) {
        dataStore.edit {
            it[KEY_TOKEN]   = token
            it[KEY_USER_ID] = userId
            it[KEY_NAME]    = name
        }
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }
}