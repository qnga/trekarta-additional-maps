package org.qnga.trekarta.maps.core.provider

import android.database.AbstractCursor

class TileCursor(private val tileUrl: String) : AbstractCursor() {

    override fun getCount(): Int {
        return ContentProviderContract.TILE_COLUMNS.size
    }

    override fun getColumnNames(): Array<String> {
        return ContentProviderContract.TILE_COLUMNS
    }

    override fun getString(column: Int): String {
        require(column == 0)
        return tileUrl
    }

    override fun getShort(column: Int): Short {
       throw IllegalArgumentException()
    }

    override fun getInt(column: Int): Int {
        throw IllegalArgumentException()
    }

    override fun getLong(column: Int): Long {
        throw IllegalArgumentException()
    }

    override fun getFloat(column: Int): Float {
        throw IllegalArgumentException()
    }

    override fun getDouble(column: Int): Double {
        throw IllegalArgumentException()
    }

    override fun isNull(column: Int): Boolean {
        require(column == 0)
        return false
    }
}
