package helpers

import android.annotation.SuppressLint
import android.widget.ProgressBar
import android.widget.TextView

@SuppressLint("SetTextI18n")
fun updateProgressBar(progressPercent: Double, progressbar: ProgressBar, progresspecent: TextView) {
    val result = (progressPercent * 100).toInt()
    progressbar.progress = result
    progresspecent.text = "$result%"
}