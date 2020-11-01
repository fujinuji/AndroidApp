package ro.ubb.cs.fujinuji.androidapp.data

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object ItemApi {
    private const val URL = "http://192.168.1.2:3000/"

    interface Service {
        @GET("/flight")
        suspend fun find(): List<Flight>

        @GET("/flight/{id}")
        suspend fun read(@Path("id") itemId: String): Flight;

        @Headers("Content-Type: application/json")
        @POST("/flight")
        suspend fun create(@Body item: Flight): Flight

        @Headers("Content-Type: application/json")
        @PUT("/flight/{id}")
        suspend fun update(@Path("id") itemId: String, @Body item: Flight): Flight
    }

    private val client: OkHttpClient = OkHttpClient.Builder().build()

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val service: Service = retrofit.create(Service::class.java)
}