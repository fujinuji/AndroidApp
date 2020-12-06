package ro.ubb.cs.fujinuji.androidapp.data

data class MessageData(var event: String, var payload: FlightJson) {
    data class FlightJson(var plant: Flight)
}