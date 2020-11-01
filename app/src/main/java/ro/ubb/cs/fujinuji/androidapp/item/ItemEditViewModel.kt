package ro.ubb.cs.fujinuji.androidapp.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubb.cs.fujinuji.androidapp.core.TAG
import ro.ubb.cs.fujinuji.androidapp.data.Flight
import ro.ubb.cs.fujinuji.androidapp.data.FlightRepository

class ItemEditViewModel : ViewModel() {
    private val mutableItem = MutableLiveData<Flight>().apply { value = Flight("", "", "", "", "") }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val item: LiveData<Flight> = mutableItem
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            Log.i(TAG, "loadItem...")
            mutableFetching.value = true
            mutableException.value = null
            try {
                mutableItem.value = FlightRepository.load(itemId)
                Log.i(TAG, "loadItem succeeded")
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadItem failed", e)
                mutableException.value = e
                mutableFetching.value = false
            }
        }
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
            try {
                if (item.id.isNotEmpty()) {
                    mutableItem.value = FlightRepository.update(item)
                } else {
                    mutableItem.value = FlightRepository.save(item)
                }
                Log.i(TAG, "saveOrUpdateItem succeeded");
                mutableCompleted.value = true
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "saveOrUpdateItem failed", e);
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }
}