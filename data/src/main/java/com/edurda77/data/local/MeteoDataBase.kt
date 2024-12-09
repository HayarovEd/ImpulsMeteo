package com.edurda77.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteEntity::class],
    version = 1
)
abstract class MeteoDataBase : RoomDatabase() {
    abstract val meteoDao: MeteoDao
}