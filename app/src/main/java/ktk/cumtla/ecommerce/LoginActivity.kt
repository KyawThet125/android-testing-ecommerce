package ktk.cumtla.ecommerce

import android.app.ProgressDialog.show
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import ktk.cumtla.ecommerce.libby.H.Companion.USER_TOKEN
import ktk.cumtla.ecommerce.libby.H.Companion.l
import ktk.cumtla.ecommerce.modals.Category
import ktk.cumtla.ecommerce.modals.Token
import ktk.cumtla.ecommerce.services.ServiceBuilder
import ktk.cumtla.ecommerce.services.WebServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Login"
        setContentView(R.layout.activity_main)
        loginAds.isSelected = true
        LoginBtn.setOnClickListener {
            val email = loginEmail.text.toString()
            val pass = loginPass.text.toString()
            loginUser(email, pass)

        }

        loginCancelBtn.setOnClickListener {
            loginEmail.text = null
            loginPass.text = null
        }

        toRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }


    private fun loginUser(email: String, pass: String) {
        val services: WebServices = ServiceBuilder.buildService((WebServices::class.java))
        val responseToken: Call<Token> = services.loginUser(email, pass)

        responseToken.enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>, t: Throwable) {
                l(t.message.toString())
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                val token: Token = response.body()!!
                USER_TOKEN = token.token
                val intent = Intent(this@LoginActivity,CategoryActivity::class.java)
                startActivity(intent)
            }

        })
    }


}
