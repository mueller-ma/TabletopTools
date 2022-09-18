package com.github.muellerma.tabletoptools.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.muellerma.tabletoptools.ui.fragments.custom_dice.DieDao

@Database(entities = [CustomDie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dieDao(): DieDao

    companion object {
        private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_db"
                ).build()
            }

            return instance!!
        }
    }
}