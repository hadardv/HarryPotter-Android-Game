package com.example.harrypottergame

import android.os.Bundle
import android.view.inputmethod.InputBinding
import androidx.appcompat.app.AppCompatActivity
import com.example.harrypottergame.databinding.ActivityRecordsBinding
import fragments.HighScoreFragment
import fragments.MapFragment

class RecordsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecordsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        if (supportFragmentManager.findFragmentById(R.id.records_FRAME_scores) == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.records_FRAME_scores, HighScoreFragment())
                .commit()
        }

        if (supportFragmentManager.findFragmentById(R.id.records_FRAME_map) == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.records_FRAME_map, MapFragment())
                .commit()
        }
    }
}
