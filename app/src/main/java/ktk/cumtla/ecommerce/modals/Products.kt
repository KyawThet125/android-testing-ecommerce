package ktk.cumtla.ecommerce.modals

import com.google.gson.annotations.SerializedName

class Products(
    val current_page: Int,
    @SerializedName("data")
    val products: List<Product>
)