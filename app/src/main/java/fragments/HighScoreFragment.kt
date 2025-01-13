package fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.harrypottergame.R
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.example.harrypottergame.Record

class HighScoreFragment : Fragment() {

    @SuppressLint("DefaultLocale")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_high_score, container, false)
        val scoresTextView = view.findViewById<TextView>(R.id.highScore_TXT_scores)

        // Load the top scores from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("game_records", Context.MODE_PRIVATE)
        val scoresJson = sharedPreferences.getString("scores", "[]") ?: "[]"
        val scoresList: List<Record> = Gson().fromJson(scoresJson, object : TypeToken<List<Record>>() {}.type)

        // Display the scores
        val scoresText = StringBuilder()
        scoresList.forEachIndexed { index, record ->
            scoresText.append("${index + 1}. Score: ${record.score}, Location: [${String.format("%.2f", record.latitude)}, ${String.format("%.2f", record.longitude)}]\n")
        }
        scoresTextView.text = scoresText.toString()

        return view
    }
}
