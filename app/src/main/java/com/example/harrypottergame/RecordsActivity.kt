package com.example.harrypottergame

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import adapters.ScoresAdapter
import android.content.Context
import com.google.android.gms.maps.model.MarkerOptions


class RecordsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        // Get the map fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.records_FRAME_map) as SupportMapFragment

        // Load the scores from SharedPreferences
        val sharedPreferences = getSharedPreferences("game_records", Context.MODE_PRIVATE)
        val scoresJson = sharedPreferences.getString("scores", "[]") ?: "[]"
        val scoresList: List<com.example.harrypottergame.Record> = Gson().fromJson(
            scoresJson, object : TypeToken<List<com.example.harrypottergame.Record>>() {}.type
        )

        // Setup the ListView with an adapter
        val scoresAdapter = ScoresAdapter(this, scoresList)
        val listView: ListView = findViewById(R.id.records_LIST_scores)
        listView.adapter = scoresAdapter

        // Handle clicks on the score records
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedRecord = scoresList[position]

            // Clear all markers and add one for the selected record
            mapFragment.getMapAsync { googleMap ->
                googleMap.clear()
                val location = LatLng(selectedRecord.latitude, selectedRecord.longitude)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title("Score: ${selectedRecord.score}")
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
            }
        }

    }
}
