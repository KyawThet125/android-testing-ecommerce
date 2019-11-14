package ktk.cumtla.ecommerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_category.*
import ktk.cumtla.ecommerce.adapter.CategoryAdapter
import ktk.cumtla.ecommerce.libby.H.Companion.USER_TOKEN
import ktk.cumtla.ecommerce.libby.H.Companion.checkUserAuth
import ktk.cumtla.ecommerce.libby.H.Companion.l
import ktk.cumtla.ecommerce.modals.Category
import ktk.cumtla.ecommerce.services.ServiceBuilder
import ktk.cumtla.ecommerce.services.WebServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        if (checkUserAuth()) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        categoryRecycler.layoutManager = GridLayoutManager(this, 2)

        loadAllCategory()


    }

    fun loadAllCategory() {
        val services: WebServices = ServiceBuilder.buildService(WebServices::class.java)
        val responseCats: Call<List<Category>> = services.getAllCat("Bearer $USER_TOKEN")

        responseCats.enqueue(object : Callback<List<Category>> {
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                l(t.message!!)
            }

            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                if (response.isSuccessful) {
                    val cats: List<Category> = response.body()!!
                    categoryRecycler.adapter = CategoryAdapter(this@CategoryActivity, cats)
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
