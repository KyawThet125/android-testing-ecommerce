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
import kotlinx.android.synthetic.main.activity_single_category_products.*
import ktk.cumtla.ecommerce.adapter.ProductAdapter
import ktk.cumtla.ecommerce.libby.H
import ktk.cumtla.ecommerce.libby.H.Companion.USER_TOKEN
import ktk.cumtla.ecommerce.libby.H.Companion.l
import ktk.cumtla.ecommerce.modals.Products
import ktk.cumtla.ecommerce.services.ServiceBuilder
import ktk.cumtla.ecommerce.services.WebServices
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class singleCategoryProducts : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_category_products)
        val bundle: Bundle = intent.extras!!
        var catId = bundle.getString("cat_id")
        if (H.checkUserAuth()) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        singleCategoryProductRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        loadAllProductsOfACategory(catId.toString())

    }

    private fun loadAllProductsOfACategory(catId: String) {
        val services: WebServices = ServiceBuilder.buildService(WebServices::class.java)
        val responseProducts: Call<Products> =
            services.getProductsOfACategory("Bearer $USER_TOKEN", catId)

        responseProducts.enqueue(object : Callback<Products> {
            override fun onFailure(call: Call<Products>, t: Throwable) {
                l(t.message.toString())
            }

            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    val prod: Products = response.body()!!
                    val products = prod.products
                    singleCategoryProductRecycler.adapter =
                        ProductAdapter(this@singleCategoryProducts, products)
                } else {
                    l("Something is wrong")
                }
            }

        })
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val item: MenuItem = menu.findItem(R.id.cart)
        MenuItemCompat.setActionView(item, R.layout.my_cart_layout)
        return super.onCreateOptionsMenu(menu)
    }
}
