package ipca.pdm.a6160a9710.ui.areas

import android.content.Context
import android.content.Context.MODE_PRIVATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

object AreasBackend {
    private val client = OkHttpClient()

    fun fetchAreas(context: Context, scope: CoroutineScope, callback: (ArrayList<Area>) -> Unit) {
        scope.launch (Dispatchers.IO) {
            val sh = context.getSharedPreferences("SharedPref", MODE_PRIVATE)
            val token = sh.getString("token", "")

            val request = Request.Builder()
                .url("https://setr.braintechcloud.com/areas/")
                .header("Cookie", "token=${token}")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        val areas = arrayListOf<Area>()

                        if (response.isSuccessful) {
                            val result = response.body!!.string()
                            val jsonArray = JSONArray(result)

                            for (i in 0 until jsonArray.length()) {
                                val jsonArea = jsonArray.getJSONObject(i)
                                val area = Area(jsonArea.getInt("id"), jsonArea.getString("name"))
                                areas.add(area)
                            }
                        }

                        scope.launch (Dispatchers.Main) {
                            callback(areas)
                        }
                    }
                }
            })
        }
    }
}