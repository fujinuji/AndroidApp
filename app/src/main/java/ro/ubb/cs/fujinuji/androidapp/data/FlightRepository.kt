package ro.ubb.cs.fujinuji.androidapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ro.ubb.cs.fujinuji.androidapp.core.Constants
import ro.ubb.cs.fujinuji.androidapp.data.local.FlightDao
import ro.ubb.cs.fujinuji.androidapp.data.remote.ItemApi
import ro.ubb.cs.fujinuji.androidapp.core.Result

class FlightRepository(private val flightDao: FlightDao) {

    val flights = MediatorLiveData<List<Flight>>().apply { postValue(emptyList()) }

    suspend fun refresh(): Result<Boolean> {
        try {
            val flightsApi = ItemApi.service.find()
            flights.value = flightsApi
            for (flight in flightsApi) {
                //plant.userId = Constants.instance()?.fetchValueString("_id")!!
                flightDao.insert(flight)
            }
            return Result.Success(true)
        } catch (e: Exception) { // handle offline mode

            val userId = Constants.instance()?.fetchValueString("_id")
            flights.addSource(flightDao.getAll(userId!!)) {
                flights.value = it
            }

            return Result.Error(e)
        }
    }

    fun getById(itemId: String): LiveData<Flight> {
        return flightDao.getById(itemId)
    }

    suspend fun save(item: Flight): Result<Flight> {
        try {
            val createdItem = ItemApi.service.create(item)
            flightDao.insert(createdItem)
            return Result.Success(createdItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(item: Flight): Result<Flight> {
        try {
            val updatedItem = ItemApi.service.update(item._id, item)
            flightDao.update(updatedItem)
            return Result.Success(updatedItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun delete(itemId: String): Result<Boolean> {
        try {
            ItemApi.service.delete(itemId)
            flightDao.delete(id = itemId)
            return Result.Success(true)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
}