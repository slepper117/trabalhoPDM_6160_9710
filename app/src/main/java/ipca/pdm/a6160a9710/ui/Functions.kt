package ipca.pdm.a6160a9710.ui

import java.text.SimpleDateFormat
import java.util.*

fun String.parseDate() : Date? {
    // 2023-01-06T21:09:08.000Z
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return format.parse(this)
}

fun String.parseDateTZ() : Date? {
    // 2023-01-06T21:09:08+00:00
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return format.parse(this.substring(0, this.length - 6))
}

fun Date.parseDateFull() : String {
    // Wed, 4 Jul 2001 12:08:56
    val format = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault())
    return format.format(this)
}
fun Long.parseLongToDateString(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return format.format(date)
}

fun Date.parseDateISO() : String {
    // 2023-01-06T21:09:08.000Z
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return format.format(this)
}