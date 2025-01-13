package adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ArrayAdapter
import com.example.harrypottergame.Record
import android.annotation.SuppressLint
import com.example.harrypottergame.R

class ScoresAdapter(context: Context, scores: List<Record>) : ArrayAdapter<Record>(context, 0, scores) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val record = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_score, parent, false)

        val scoreTextView: TextView = view.findViewById(R.id.item_score_text)
        scoreTextView.text = "Score: ${record?.score}, Location: [${record?.latitude}, ${record?.longitude}]"

        return view
    }
}
