package org.qnga.trekarta.maps.core.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MapEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mapDao(): MapDao

    companion object {

        private lateinit var instance: AppDatabase

        fun get(application: Application): AppDatabase {
            if (::instance.isInitialized) {
                return instance
            }

            instance = Room.databaseBuilder(
                application,
                AppDatabase::class.java, "database"
            ).build()

            return instance
        }
    }
}
