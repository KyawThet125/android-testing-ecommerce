package ktk.cumtla.ecommerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_my_cart.*
import ktk.cumtla.ecommerce.adapter.CartAdapter
import ktk.cumtla.ecommerce.libby.H
import ktk.cumtla.ecommerce.libby.H.Companion.USER_TOKEN
import ktk.cumtla.ecommerce.modals.CartProduct
import ktk.cumtla.ecommerce.modals.ErrorMessenger
import ktk.cumtla.ecommerce.services.ServiceBuilder
import ktk.cumtla.ecommerce.services.WebServices
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCart : AppCompatActivity() {
    var cartCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)

        supportActionBar?.title = "My Cart's Item"

        val cartKeys = H.getAllKeys()
        H.l("$cartKeys")

        cartRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        getCartProducts(cartKeys)


    }

    fun getCartProducts(cartKeys: String) {
        val services: WebServices = ServiceBuilder.buildService(WebServices::class.java)
        val responseProducts: Call<List<CartProduct>> =
            services.getPreviewCartItems("Bearer $USER_TOKEN", cartKeys)
        responseProducts.enqueue(object : Callback<List<CartProduct>> {
            override fun onFailure(call: Call<List<CartProduct>>, t: Throwable) {
                H.l(t.message!!)
            }

            override fun onResponse(call: Call<List<CartProduct>>, response: Response<List<CartProduct>>
            ) {
                if (response.isSuccessful) {
                    val products: List<CartProduct> = response.body()!!
                   cartRecycler.adapter = CartAdapter(this@MyCart,products)
                } else {
                    H.l("Something is wrong!")
                }
            }
        })
    }

    fun  billOut(){
        val cartKeys = H.getAllKeys()
        val services: WebServices = ServiceBuilder.buildService(WebServices::class.java)
        val responseMessage: Call<ErrorMessenger> = services.billOutOrder("Bearer $USER_TOKEN", cartKeys)
        responseMessage.enqueue(object : Callback<ErrorMessenger>{
            override fun onFailure(call: Call<ErrorMessenger>, t: Throwable) {
                H.l(t.message!!)
            }

            override fun onResponse(call: Call<ErrorMessenger>, response: Response<ErrorMessenger>) {
                if(response.isSuccessful){
                    val message = response.body()!!
                    toast(message.msg)
                    H.clearCart()
                }else {
                    H.l("Something Wrong")
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.billOut){
            billOut()
        }else if(item.itemId == R.id.product_upload){
            startActivity(Intent(this@MyCart,ProductUploadd::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}
