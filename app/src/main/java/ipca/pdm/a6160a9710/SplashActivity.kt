package ipca.pdm.a6160a9710

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val sh = getSharedPreferences("SharedPref", MODE_PRIVATE)

        lifecycleScope.launch(Dispatchers.Main) {
            delay(2000)
            val token = sh.getString("token", "")
            if (token?.length  != 0) startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            else startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        }
    }
}