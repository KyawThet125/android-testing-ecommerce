package ktk.cumtla.ecommerce.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.category_row.view.*
import ktk.cumtla.ecommerce.R
import ktk.cumtla.ecommerce.modals.Category
import ktk.cumtla.ecommerce.singleCategoryProducts

class CategoryAdapter(val context: Context, val cats: List<Category>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.category_row, parent, false))
    }

    override fun getItemCount(): Int = cats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cat = cats[position]
        holder.itemView.categoryName.text = cat.name;
        holder.itemView.categoryName.setOnClickListener {
            val intent = Intent(context, singleCategoryProducts::class.java)
             intent.putExtra("cat_id", cat.id.toString())
             context.startActivity(intent)

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}