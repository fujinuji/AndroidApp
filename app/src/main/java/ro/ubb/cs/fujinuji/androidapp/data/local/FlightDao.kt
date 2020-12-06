package ro.ubb.cs.fujinuji.androidapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.ubb.cs.fujinuji.androidapp.data.Flight

@Dao
interface FlightDao {

    @Query("SELECT * FROM flights WHERE userId=:userId")
    fun getAll(userId: String): LiveData<List<Flight>>

    @Query("SELECT * FROM flights WHERE _id=:id")
    fun getById(id: String): LiveData<Flight>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flight: Flight)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(flight: Flight)

    @Query("DELETE FROM flights WHERE _id=:id")
    suspend fun delete(id: String)

    @Query("DELETE FROM flights")
    suspend fun deleteAll()
}