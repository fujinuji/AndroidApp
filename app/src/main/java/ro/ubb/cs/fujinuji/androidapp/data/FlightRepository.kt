package ro.ubb.cs.fujinuji.androidapp.data

import android.util.Log
import ro.ubb.cs.fujinuji.androidapp.core.TAG

object FlightRepository {
    private var cachedItems: MutableList<Flight>? = null;

    suspend fun loadAll(): List<Flight> {
        Log.i(TAG, "loadAll")
        if (cachedItems != null) {
            return cachedItems as List<Flight>;
        }
        cachedItems = mutableListOf()
        val items = ItemApi.service.find()
        cachedItems?.addAll(items)
        return cachedItems as List<Flight>
    }

    suspend fun load(itemId: String): Flight {
        Log.i(TAG, "load")
        val item = cachedItems?.find { it.id == itemId }
        if (item != null) {
            return item
        }
        return ItemApi.service.read(itemId)
    }

    suspend fun save(item: Flight): Flight {
        Log.i(TAG, "save")
        val createdItem = ItemApi.service.create(item)
        cachedItems?.add(createdItem)
        return createdItem
    }

    suspend fun update(item: Flight): Flight {
        Log.i(TAG, "update")
        val updatedItem = ItemApi.service.update(item.id, item)
        val index = cachedItems?.indexOfFirst { it.id == item.id }
        if (index != null) {
            cachedItems?.set(index, updatedItem)
        }
        return updatedItem
    }
}