package ipca.pdm.a6160a9710

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object RootBackend {
    private val client = OkHttpClient()

    fun login(context: Context, scope: CoroutineScope, json: JSONObject, callback: (Boolean) -> Unit) {
        scope.launch (Dispatchers.IO) {
            val sh = context.getSharedPreferences("SharedPref", MODE_PRIVATE)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = json.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url("https://setr.braintechcloud.com/auth/login")
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
                            val cookie = response.header("Set-Cookie")
                            val token = cookie!!.substring(6, cookie.length - 27)

                            val jwt = JWT(token)
                            val claimID = jwt.getClaim("id").asInt()

                            val setUser = sh.edit()
                            setUser.putString("token", token)
                            setUser.putInt("userID", claimID!!)
                            setUser.apply()

                            result = true
                        }

                        scope.launch(Dispatchers.Main) {
                            callback(result)
                        }
                    }
                }
            })
        }
    }
}