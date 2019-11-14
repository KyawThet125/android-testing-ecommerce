package ktk.cumtla.ecommerce.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_row.view.*
import ktk.cumtla.ecommerce.R
import ktk.cumtla.ecommerce.SingleProduct
import ktk.cumtla.ecommerce.modals.CartProduct
import ktk.cumtla.ecommerce.modals.Product

class CartAdapter(val context: Context, val products: List<CartProduct>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_row, parent, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.itemView.tvTitle.text = "${product.name} || ${product.count}"
        Picasso.get().load(product.image).into(holder.itemView.tvImage)
        holder.itemView.tvPrice.text = product.price.toString()


    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}