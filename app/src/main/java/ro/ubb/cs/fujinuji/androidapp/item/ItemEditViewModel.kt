package ro.ubb.cs.fujinuji.androidapp.item

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ro.ubb.cs.fujinuji.androidapp.core.TAG
import ro.ubb.cs.fujinuji.androidapp.data.Flight
import ro.ubb.cs.fujinuji.androidapp.data.FlightRepository
import ro.ubb.cs.fujinuji.androidapp.data.local.FlightDatabase
import ro.ubb.cs.fujinuji.androidapp.core.Result
class ItemEditViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableItem = MutableLiveData<Flight>().apply { value = Flight("", "", "", "", "", "") }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val item: LiveData<Flight> = mutableItem
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    val flightRepository: FlightRepository

    init {
        val flightDao = FlightDatabase.getDatabases(application, viewModelScope).flightDao()
        flightRepository = FlightRepository(flightDao)
    }

    fun loadItem(itemId: String): LiveData<Flight> {
        return flightRepository.getById(itemId)
    }

    fun saveOrUpdateItem(departureTown: String, arrivalTown: String, departureTime: String, arrivalTime: String) {
        viewModelScope.launch {
            Log.i(TAG, "saveOrUpdateItem...");
            val item = mutableItem.value ?: return@launch
            item.departureTown = departureTown
            item.arrivalTown = arrivalTown
            item.departureTime = departureTime
            item.arrivalTime = arrivalTime
            mutableFetching.value = true
            mutableException.value = null

            val result: Result<Flight>

            if (item._id.isNotEmpty()) {
                result = flightRepository.update(item)
            } else {
                result = flightRepository.save(item)
            }

            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateItem succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }

            Log.i(TAG, "saveOrUpdateItem succeeded");
            mutableCompleted.value = true
            mutableFetching.value = false

        }
    }
}