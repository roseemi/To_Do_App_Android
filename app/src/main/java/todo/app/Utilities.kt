package todo.app

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Utilities {
    companion object {
        fun formatDate(timestamp: Long): String {
            val date = Date(timestamp)
            val formatter = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
            return formatter.format(date)
        }
    }
}