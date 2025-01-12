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
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import Record

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
  val sharedPreferences = requireContext().getSharedPreferences("game_records", Context.MODE_PRIVATE)
  val scoresJson = sharedPreferences.getString("scores", "[]") ?: "[]"
  val scoresList: List<Record> = Gson().fromJson(scoresJson, object : TypeToken<List<Record>>() {}.type)

  if (scoresList.isNotEmpty()) {
   for (record in scoresList) {
    val latitude = record.latitude
    val longitude = record.longitude

    if (latitude != 0.0 || longitude != 0.0) {
     val location = LatLng(latitude, longitude)
     googleMap?.addMarker(MarkerOptions().position(location).title("Score: ${record.score}"))
    }
   }

   // Focus the camera on the first valid location
   val firstValidRecord = scoresList.firstOrNull { it.latitude != 0.0 && it.longitude != 0.0 }
   if (firstValidRecord != null) {
    val targetLocation = LatLng(firstValidRecord.latitude, firstValidRecord.longitude)
    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, 8f))
   } else {
    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 1f))
   }
  }
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
