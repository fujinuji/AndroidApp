package ro.ubb.cs.fujinuji.androidapp.items

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ubb.cs.fujinuji.androidapp.core.TAG
import ro.ubb.cs.fujinuji.androidapp.data.Flight
import ro.ubb.cs.fujinuji.androidapp.data.FlightRepository
import ro.ubb.cs.fujinuji.androidapp.core.Result
import ro.ubb.cs.fujinuji.androidapp.data.local.FlightDatabase
import ro.ubb.cs.fujinuji.androidapp.data.remote.ItemApi

class ItemListViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableItems = MutableLiveData<List<Flight>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    var items: LiveData<List<Flight>> = mutableItems
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    private val flightRepository: FlightRepository

    init {
        val flightDao = FlightDatabase.getDatabases(application, viewModelScope).flightDao()
        flightRepository = FlightRepository(flightDao)
        items = flightRepository.flights

        CoroutineScope(Dispatchers.Main).launch { collectEvents() }
    }

    suspend fun collectEvents() {
        while (true) {
            val event = ItemApi.RemoteDataSource.eventChannel.receive()
            Log.d("ws", event)
            Log.d("MainActivity", "received $event")
            refresh()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...")
            mutableLoading.value = true
            mutableException.value = null
            when (val result = flightRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded")
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception)
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}