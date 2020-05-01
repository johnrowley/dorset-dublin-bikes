package com.aaademo.mapdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.aaademo.mapdemo.model.BikeStation
import com.aaademo.mapdemo.model.Stations

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var stationList: List<BikeStation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }


    fun getBikeStationJsonData() {

        Log.i(getString(R.string.DEBUG_MAINACTIVITY), "Loading JSON data")

        var url = getString(R.string.DUBLIN_BIKE_API_URL) + getString(R.string.DUBLIN_BIKE_API_KEY)

        Log.i(getString(R.string.DEBUG_MAINACTIVITY), url)

        //Create a request object

        val request = Request.Builder().url(url).build()

        //Create a client

        val client = OkHttpClient()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //   TODO("Not yet implemented")
                Log.i(getString(R.string.DEBUG_MAINACTIVITY), "JSON HTTP CALL FAILED")
            }

            override fun onResponse(call: Call, response: Response) {
                // TODO("Not yet implemented")
                Log.i(getString(R.string.DEBUG_MAINACTIVITY), "JSON HTTP CALL SUCCEEDED")

                val body = response?.body?.string()
                //  println("json loading" + body)
                Log.i(getString(R.string.DEBUG_MAINACTIVITY), body)
                var jsonBody = "{\"stations\": " + body + "}"

                val gson = GsonBuilder().create()
                stationList = gson.fromJson(jsonBody, Stations::class.java).stations

                renderMarkers()


            }


        })


    }

    fun renderMarkers() {

        runOnUiThread {

            stationList.forEach {
                val position = LatLng(it.position.lat, it.position.lng)
                var marker = mMap.addMarker(
                    MarkerOptions().position(position).title("Marker in ${it.address}")
                )
                marker.setTag(it.number)
                Log.i(getString(R.string.DEBUG_MAINACTIVITY), "${it.address} : ${it.position.lat} : ${it.position.lng}")
            }


             val centreLocation = LatLng(53.349562, -6.278198)
             mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centreLocation, 16.0f))

        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        Log.i(getString(R.string.DEBUG_MAINACTIVITY), "renderMarkers called")


        mMap = googleMap
        getBikeStationJsonData()
        /*
            var listOfBikeStations = listOf(
                BikeStation(42, "Smithfield North",  53.349562, -6.278198),
                BikeStation(30,"Parnell Square North", 53.353462, -6.265305),
                BikeStation( 54,"Clonmel Street", 53.336021, -6.26298)

            )

            */

/*
        listOfBikeStations.forEach {
            val position = LatLng(it.lat, it.lng)
            var marker1 = mMap.addMarker(MarkerOptions().position(position).title("Marker in ${it.address}"))
            marker1.setTag(it.number)
            Log.i("MAINACTIVTY", it.address)
        }
        val centreLocation = LatLng(53.349562, -6.278198)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centreLocation, 12.0f))

*/


/*

        val smithfield = LatLng(53.349562, -6.278198)
        var marker1 = mMap.addMarker(MarkerOptions().position(smithfield).title("Marker in smithfield"))
        marker1.setTag(42)


        val Parnell = LatLng(53.353462, -6.265305)
        var marker2 = mMap.addMarker(MarkerOptions().position(Parnell).title("Marker in Parnell"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Parnell))
        marker2.setTag(30)


        val Clonmel = LatLng(53.336021, -6.26298)
        var marker3 = mMap.addMarker(MarkerOptions().position(Clonmel).title("Marker in Clonmel"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Clonmel))
        marker3.setTag(54)
*/

        mMap.setOnMarkerClickListener { marker ->


            if (marker.isInfoWindowShown) {

                marker.hideInfoWindow()
            } else {

                marker.showInfoWindow()
            }


            Log.i(getString(R.string.DEBUG_MAINACTIVITY), "Marker is clicked")
            Log.i(getString(R.string.DEBUG_MAINACTIVITY), "Marker id (tag) is " + marker.getTag().toString())
            Log.i(getString(R.string.DEBUG_MAINACTIVITY), "Marker address is  " + marker.title)


            true
        }



    }
}
