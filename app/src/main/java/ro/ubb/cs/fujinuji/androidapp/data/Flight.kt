package ro.ubb.cs.fujinuji.androidapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flights")
data class Flight (
    @PrimaryKey @ColumnInfo(name = "_id") val _id: String,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "departureTown") var departureTown: String,
    @ColumnInfo(name = "arrivalTown") var arrivalTown: String,
    @ColumnInfo(name = "departureTime") var departureTime: String,
    @ColumnInfo(name = "arrivalTime") var arrivalTime: String
) {
    override fun toString(): String {
        return "$departureTown, $arrivalTown, $departureTime, $arrivalTime"
    }
}