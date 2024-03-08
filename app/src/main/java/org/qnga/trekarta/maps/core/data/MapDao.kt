package org.qnga.trekarta.maps.core.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity
data class MapEntity(
    @PrimaryKey val id: String,
    val settings: String
)

@Dao
interface MapDao {

    @Delete
    suspend fun delete(map: MapEntity)

    @Upsert
    suspend fun upsert(map: MapEntity)

    @Query("SELECT * FROM MapEntity WHERE id = :id")
    suspend fun get(id: String): MapEntity

    @Query("SELECT * FROM MapEntity")
    fun getAll(): Flow<List<MapEntity>>
}
