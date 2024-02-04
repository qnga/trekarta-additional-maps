package org.qnga.trekarta.maps.core.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity
data class MapEntity(
    @PrimaryKey val id: String,
    val settings: String
)

@Dao
interface MapDao {
    @Insert
    suspend fun insert(map: MapEntity)

    @Delete
    suspend fun delete(map: MapEntity)

    @Update
    suspend fun update(map: MapEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(map: MapEntity)

    @Query("SELECT * FROM MapEntity WHERE id = :id")
    suspend fun get(id: String): MapEntity?

    @Query("SELECT * FROM MapEntity")
    fun getAll(): Flow<List<MapEntity>>
}
