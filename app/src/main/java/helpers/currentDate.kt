package helpers

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDate(): String{
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("E yyyy-MM-dd")
    return current.format(formatter)
}