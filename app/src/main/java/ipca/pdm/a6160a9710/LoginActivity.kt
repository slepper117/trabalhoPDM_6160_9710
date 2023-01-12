package ipca.pdm.a6160a9710

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import ipca.pdm.a6160a9710.databinding.ActivityLoginBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()

            val json = JSONObject()
            json.put("username", username)
            json.put("password", password)

            RootBackend.login(baseContext, lifecycleScope, json) {
                if (it) startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                else Toast.makeText(baseContext, "Autenticação Falhada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}