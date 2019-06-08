package com.courtney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_bus.*
import kotlinx.android.synthetic.main.row_bus.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import java.net.URL

class BusActivity : AppCompatActivity() {

    var bus : List<Bus>? = null
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.myjson.com/bins/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)

        doAsync {
            val BusService = retrofit.create(BusService::class.java)
            bus = BusService.listBus()
                .execute()
                .body()
            uiThread {
                recycler.apply {
                    layoutManager = LinearLayoutManager(this@BusActivity)
                    setHasFixedSize(true)
                    adapter = BusAdapter()
                }
            }
        }
    }

    inner class BusAdapter : RecyclerView.Adapter<BusHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_bus, parent, false)
            return BusHolder(view)
        }

        override fun getItemCount(): Int {
            return bus!!.size
        }

        override fun onBindViewHolder(holder: BusHolder, position: Int) {
            holder.routeText.text = bus!![position].RouteID
            holder.busText.text = bus!![position].BusID
            holder.speedText.text = bus!![position].Speed
        }

    }

    inner class BusHolder(view: View) : RecyclerView.ViewHolder(view) {
        val routeText = view.txt_route_id
        val busText = view.txt_bus_id
        val speedText = view.txt_speed
    }
}

data class Bus(
    val Azimuth: String,
    val BusID: String,
    val BusStatus: String,
    val DataTime: String,
    val DutyStatus: String,
    val GoBack: String,
    val Latitude: String,
    val Longitude: String,
    val ProviderID: String,
    val RouteID: String,
    val Speed: String,
    val ledstate: String,
    val sections: String
)

interface BusService {
    @GET("ar7tp")
    fun listBus() : Call<List<Bus>>
}