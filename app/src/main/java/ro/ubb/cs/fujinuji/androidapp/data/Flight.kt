package ro.ubb.cs.fujinuji.androidapp.data

data class Flight (
    val id: String,
    var departureTown: String,
    var arrivalTown: String,
    var departureTime: String,
    var arrivalTime: String
) {
    override fun toString(): String {
        return "'$departureTown', '$arrivalTown', '$departureTime', '$arrivalTime')"
    }
}