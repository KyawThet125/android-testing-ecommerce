package ktk.cumtla.ecommerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title ="RegisterActivity"
        setContentView(R.layout.register)
        registerBtn.setOnClickListener {
            val username = registerUsername.text
            val email = registerEmail.text
            val password = registerPass.text
        }
        registerCancelBtn.setOnClickListener {
            registerUsername.text = null
            registerEmail.text = null
            registerPass.text = null
        }

        toLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
