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
import ktk.cumtla.ecommerce.modals.Product

class ProductAdapter(val context: Context, val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.product_row, parent, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.itemView.tvTitle.text = product.name
        Picasso.get().load(product.image).into(holder.itemView.tvImage)
        holder.itemView.tvPrice.text = product.price.toString()
        holder.itemView.btnDetail.setOnClickListener {
            val intent = Intent(context, SingleProduct::class.java)
            intent.putExtra("product", product)
            context.startActivity(intent)
        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}