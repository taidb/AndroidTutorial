package com.eco.musicplayer.audioplayer.music.activity.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class UserProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.eco.musicplayer.audioplayer.music.activity.contentprovider.User"
        val USERS_URI: Uri = Uri.parse("content://$AUTHORITY/user")
    }

    private lateinit var db: AppDatabase

    override fun onCreate(): Boolean {
        db = AppDatabase.getInstance(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        return db.userDao.getAllUsersCursor()
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val name = values?.getAsString("name") ?: return null

        runBlocking(Dispatchers.IO) {
            db.userDao.insertUser(User(name = name))
        }

        return USERS_URI
    }

    override fun getType(uri: Uri): String = "vnd.android.cursor.dir/users"
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
