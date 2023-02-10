package ipca.pdm.a6160a9710.ui.home

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ipca.pdm.a6160a9710.ui.areas.Area
import ipca.pdm.a6160a9710.ui.bookings.Booking
import ipca.pdm.a6160a9710.ui.clocks.Clock
import ipca.pdm.a6160a9710.ui.parseDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

object HomeBackend {
    private val client = OkHttpClient()

    fun getUser(context: Context, scope: CoroutineScope, callback: (User) -> Unit) {
        scope.launch (Dispatchers.IO) {
            val sh = context.getSharedPreferences("SharedPref", MODE_PRIVATE)
            val token = sh.getString("token", "")
            val userID = sh.getInt("userID", 1)

            val request = Request.Builder()
                .url("https://setr.braintechcloud.com/users/${userID}")
                .header("Cookie", "token=${token}")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (response.isSuccessful) {
                            val json = JSONObject(response.body!!.string())
                            val roleJson = json.getJSONObject("role")
                            val arrayAreas = json.getJSONArray("areas")
                            val arrayClocks = json.getJSONArray("lastclocks")
                            val arrayBookings = json.getJSONArray("nextbookings")
                            val user = User(json.getString("name"), roleJson.getString("name"))

                            if (arrayAreas.length() > 0) {
                                val areas = arrayListOf<Area>()

                                for (i in 0 until arrayAreas.length()) {
                                    val jsonArea = arrayAreas.getJSONObject(i)
                                    val area = Area(jsonArea.getInt("id"), jsonArea.getString("name"))
                                    areas.add(area)
                                }

                                user.userAreas = areas
                            }

                            if (arrayClocks.length() > 0) {
                                val clocks = arrayListOf<Clock>()

                                for (i in 0 until arrayClocks.length()) {
                                    val jsonClock = arrayClocks.getJSONObject(i)
                                    val clock = Clock(jsonClock.getInt("id"), jsonClock.getString("direction"), jsonClock.getString("datetime").parseDate())
                                    clocks.add(clock)
                                }

                                user.userClocks = clocks
                            }

                            if (arrayBookings.length() > 0) {
                                val bookings = arrayListOf<Booking>()

                                for (i in 0 until arrayBookings.length()) {
                                    val jsonBooking = arrayBookings.getJSONObject(i)
                                    val booking = Booking(jsonBooking.getInt("id"), jsonBooking.getString("start").parseDate(), jsonBooking.getString("description"))
                                    bookings.add(booking)
                                }

                                user.userBookings = bookings
                            }

                            scope.launch (Dispatchers.Main) {
                                callback(user)
                            }
                        }
                    }
                }
            })
        }
    }
}