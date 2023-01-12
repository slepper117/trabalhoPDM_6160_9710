package ipca.pdm.a6160a9710.ui.bookings

import android.content.ClipDescription
import android.content.Context
import android.content.Context.MODE_PRIVATE
import ipca.pdm.a6160a9710.ui.parseDate
import ipca.pdm.a6160a9710.ui.parseDateISO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

object BookingsBackend {
    private val client = OkHttpClient()

    fun fetchBookings(context: Context, scope: CoroutineScope, callback: (ArrayList<Booking>) -> Unit) {
        scope.launch (Dispatchers.IO) {
            val sh = context.getSharedPreferences("SharedPref", MODE_PRIVATE)
            val token = sh.getString("token", "")

            val request = Request.Builder()
                .url("https://setr.braintechcloud.com/bookings/")
                .header("Cookie", "token=${token}")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        val bookings = arrayListOf<Booking>()

                        if (response.isSuccessful) {
                            val jsonArray = JSONArray(response.body!!.string())

                            for (i in 0 until jsonArray.length()) {
                                val jsonBooking = jsonArray.getJSONObject(i)
                                val userJson = jsonBooking.getJSONObject("user")
                                val roomJson = jsonBooking.getJSONObject("room")
                                val booking = Booking(jsonBooking.getInt("id"), jsonBooking.getString("start").parseDate(), jsonBooking.getString("final").parseDate(), jsonBooking.getString("description"), jsonBooking.getBoolean("validated"))
                                booking.userID = userJson.getInt("id")
                                booking.userName = userJson.getString("name")
                                booking.roomID = roomJson.getInt("id")
                                booking.roomName = roomJson.getString("name")
                                bookings.add(booking)
                            }

                            scope.launch (Dispatchers.Main) {
                                callback(bookings)
                            }
                        }
                    }
                }
            })
        }
    }

    fun postBooking(context: Context, scope: CoroutineScope, json: JSONObject, callback: (Boolean) -> Unit) {
        scope.launch (Dispatchers.IO) {
            val sh = context.getSharedPreferences("SharedPref", MODE_PRIVATE)
            val token = sh.getString("token", "")

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = json.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url("https://setr.braintechcloud.com/bookings/")
                .header("Cookie", "token=${token}")
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        var result = false

                        if (response.isSuccessful) {
                            result = true
                        }

                        scope.launch (Dispatchers.Main) {
                            callback(result)
                        }
                    }
                }
            })
        }
    }

    fun getBooking(context: Context, scope: CoroutineScope, id: Int, callback: (Booking) -> Unit) {
        scope.launch (Dispatchers.IO) {
            val sh = context.getSharedPreferences("SharedPref", MODE_PRIVATE)
            val token = sh.getString("token", "")

            val request = Request.Builder()
                .url("https://setr.braintechcloud.com/bookings/${id}")
                .header("Cookie", "token=${token}")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        val jsonBooking = JSONObject(response.body!!.string())
                        val userJson = jsonBooking.getJSONObject("user")
                        val roomJson = jsonBooking.getJSONObject("room")
                        val booking = Booking(jsonBooking.getInt("id"), jsonBooking.getString("start").parseDate(), jsonBooking.getString("final").parseDate(), jsonBooking.getString("description"), jsonBooking.getBoolean("validated"))
                        booking.userID = userJson.getInt("id")
                        booking.userName = userJson.getString("name")
                        booking.roomID = roomJson.getInt("id")
                        booking.roomName = roomJson.getString("name")

                        scope.launch (Dispatchers.Main) {
                            callback(booking)
                        }
                    }
                }
            })
        }
    }

    fun updateBooking(context: Context, scope: CoroutineScope, id: Int, json: JSONObject, callback: (Boolean) -> Unit) {
        scope.launch (Dispatchers.IO) {
            val sh = context.getSharedPreferences("SharedPref", MODE_PRIVATE)
            val token = sh.getString("token", "")

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = json.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url("https://setr.braintechcloud.com/bookings/${id}")
                .header("Cookie", "token=${token}")
                .put(body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        var result = false

                        if (response.isSuccessful) {
                            result = true
                        }

                        scope.launch (Dispatchers.Main) {
                            callback(result)
                        }
                    }
                }
            })
        }
    }

    fun deleteBooking(context: Context, scope: CoroutineScope, id: Int, callback: (Boolean) -> Unit) {
        scope.launch (Dispatchers.IO) {
            val sh = context.getSharedPreferences("SharedPref", MODE_PRIVATE)
            val token = sh.getString("token", "")

            val request = Request.Builder()
                .url("https://setr.braintechcloud.com/bookings/${id}")
                .header("Cookie", "token=${token}")
                .delete()
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        var result = false

                        if (response.isSuccessful) {
                            result = true
                        }

                        scope.launch (Dispatchers.Main) {
                            callback(result)
                        }
                    }
                }
            })
        }
    }
}