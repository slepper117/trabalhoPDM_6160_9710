package ipca.pdm.a6160a9710.ui.clocks

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ipca.pdm.a6160a9710.ui.parseDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object ClocksBackend {
    private val client = OkHttpClient()

    fun fetchClocks(context: Context, scope: CoroutineScope, callback: (ArrayList<Clock>) -> Unit) {
        scope.launch (Dispatchers.IO) {
            val sh = context.getSharedPreferences("SharedPref", MODE_PRIVATE)
            val token = sh.getString("token", "")
            val request = Request.Builder()
                .url("https://setr.braintechcloud.com/clocks/")
                .header("Cookie", "token=${token}")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        val clocks = arrayListOf<Clock>()

                        if (response.isSuccessful) {
                            val jsonArray = JSONArray(response.body!!.string())

                            for (i in 0 until jsonArray.length()) {
                                val jsonClock = jsonArray.getJSONObject(i)
                                val userClock = jsonClock.getJSONObject("user")
                                val clock = Clock(jsonClock.getInt("id"), jsonClock.getString("direction"), jsonClock.getString("datetime").parseDate())
                                clock.userName = userClock.getString("name")
                                clocks.add(clock)
                            }
                        }

                        scope.launch (Dispatchers.Main) {
                            callback(clocks)
                        }
                    }
                }
            })
        }
    }
}