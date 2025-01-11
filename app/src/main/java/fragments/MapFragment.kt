package fragments

import android.content.Context
import android.os.Bundle
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MapFragment : Fragment(), OnMapReadyCallback {

 private var map: GoogleMap? = null

 override fun onCreateView(
  inflater: LayoutInflater, container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View? {
  val view = inflater.inflate(R.layout.fragment_map, container, false)

  val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
  mapFragment?.getMapAsync(this)

  return view
 }

 override fun onMapReady(googleMap: GoogleMap) {
  map = googleMap

  // Load the top scores from SharedPreferences
  val sharedPreferences = requireContext().getSharedPreferences("game_records", Context.MODE_PRIVATE)
  val scoresJson = sharedPreferences.getString("scores", "[]") ?: "[]"
  val scoresList: List<Record> = Gson().fromJson(scoresJson, object : TypeToken<List<Record>>() {}.type)

  // Add markers to the map
  for (record in scoresList) {
   val location = LatLng(record.latitude, record.longitude)
   map?.addMarker(MarkerOptions().position(location).title("Score: ${record.score}"))
  }

  // Move the camera to the first location if the list is not empty
  if (scoresList.isNotEmpty()) {
   val firstLocation = LatLng(scoresList[0].latitude, scoresList[0].longitude)
   map?.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 5f))
  }
 }

 override fun onResume() {
  super.onResume()
  (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.onResume()
 }

 override fun onPause() {
  super.onPause()
  (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.onPause()
 }
}
