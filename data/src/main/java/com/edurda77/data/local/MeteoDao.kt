package com.edurda77.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.edurda77.domain.utils.FAVORITE_DEVICE_ID
import com.edurda77.domain.utils.FAVORITE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface MeteoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM $FAVORITE_TABLE")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("DELETE FROM $FAVORITE_TABLE WHERE $FAVORITE_DEVICE_ID=:deviceId")
    fun delete(deviceId: Int)
}