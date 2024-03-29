package ktk.cumtla.ecommerce.modals

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Product(
    val id: Int,
    val cat_id: Int,
    val name : String,
    val price : Int,
    val image : String,
    val description : String

    ): Parcelable