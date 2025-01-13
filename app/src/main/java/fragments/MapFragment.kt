package fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.harrypottergame.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.harrypottergame.Record

class MapFragment : Fragment(), OnMapReadyCallback {

 private var googleMap: GoogleMap? = null

 override fun onCreateView(
  inflater: LayoutInflater, container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View? {
  val view = inflater.inflate(R.layout.fragment_map, container, false)

  // Initialize the map
  val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
  mapFragment?.getMapAsync(this)

  return view
 }

 override fun onMapReady(map: GoogleMap) {
  googleMap = map

  // Load the top scores from SharedPreferences
  val sharedPreferences =
   requireContext().getSharedPreferences("game_records", Context.MODE_PRIVATE)
  val scoresJson = sharedPreferences.getString("scores", "[]") ?: "[]"
  val scoresList: List<Record> =
   Gson().fromJson(scoresJson, object : TypeToken<List<Record>>() {}.type)

  if (scoresList.isNotEmpty()) {
   val boundsBuilder = LatLngBounds.Builder()
   var hasValidCoordinates = false

   // Add markers for each record
   for (record in scoresList) {
    val latitude = record.latitude
    val longitude = record.longitude

    // Skip markers with invalid (0, 0) coordinates
    if (latitude != 0.0 || longitude != 0.0) {
     val location = LatLng(latitude, longitude)
     googleMap?.addMarker(
      MarkerOptions()
       .position(location)
       .title("Score: ${record.score}")
     )
     boundsBuilder.include(location)
     hasValidCoordinates = true
    }
   }

   // Adjust the camera to show all markers
   if (hasValidCoordinates) {
    val bounds = boundsBuilder.build()
    googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)) // Padding = 100
   }
  }
 }

 // Function to move the map camera to a specific location
 fun focusOnLocation(latitude: Double, longitude: Double) {
  val location = LatLng(latitude, longitude)
  googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
 }

 override fun onResume() {
  super.onResume()
  val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
  mapFragment?.onResume()
 }

 override fun onPause() {
  super.onPause()
  val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
  mapFragment?.onPause()
 }
}
