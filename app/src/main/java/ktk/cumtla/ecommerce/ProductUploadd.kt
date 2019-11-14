package ktk.cumtla.ecommerce

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_product_uploadd.*
import ktk.cumtla.ecommerce.libby.H
import ktk.cumtla.ecommerce.libby.H.Companion.USER_TOKEN
import ktk.cumtla.ecommerce.modals.ErrorMessenger
import ktk.cumtla.ecommerce.modals.FileInfo
import ktk.cumtla.ecommerce.services.ServiceBuilder
import ktk.cumtla.ecommerce.services.WebServices
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream

class ProductUploadd : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_uploadd)

        product_upload_image.setOnClickListener {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    toast("Permission Deny")
                } else {
                    fileUpload()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun fileUpload() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Choose Carefully"), 103)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 103 && resultCode == Activity.RESULT_OK && data != null) {
            val data: Uri = data.data!!
            val inStr: InputStream = contentResolver.openInputStream(data)!!
            val bitmap = BitmapFactory.decodeStream(inStr)
            product_upload_image.imageBitmap = bitmap

            val imagePath = getImagePath(data)
            //toast(imagePath)
            // H.l(imagePath)
            val file = File(imagePath)
            val requestBody: RequestBody =
                RequestBody.create(MediaType.parse("Multipart/form-data"), file)
            val body: MultipartBody.Part =
                MultipartBody.Part.createFormData("image", file.name, requestBody)

            val services: WebServices = ServiceBuilder.buildService(WebServices::class.java)
            val responseUpload: Call<FileInfo> = services.imageUpload("Bearer $USER_TOKEN", body)

            responseUpload.enqueue(object : Callback<FileInfo> {
                override fun onFailure(call: Call<FileInfo>, t: Throwable) {
                    H.l("Error is " + t.message.toString())
                }

                override fun onResponse(call: Call<FileInfo>, response: Response<FileInfo>) {
                    if (response.isSuccessful) {
                        val info: FileInfo = response.body()!!
                        toast(info.name)
                        productUpload(info.name)
                    } else {
                        H.l(("Something Wrong"))
                    }
                }

            })
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun productUpload(image: String) {
        val cat_id = 1
        val name = "New Product"
        val price = 33.0
        //image
        val description = "New product is coming"
        val services: WebServices = ServiceBuilder.buildService(WebServices::class.java)
        val responseMessage: Call<ErrorMessenger> = services.newProductUpload(
            "Bearer $USER_TOKEN",
            cat_id,
            name,
            price,
            image,
            description
        )
        responseMessage.enqueue(object : Callback<ErrorMessenger> {
            override fun onFailure(call: Call<ErrorMessenger>, t: Throwable) {
                H.l(t.message.toString())
            }

            override fun onResponse(
                call: Call<ErrorMessenger>,
                response: Response<ErrorMessenger>
            ) {
                if (response.isSuccessful) {
                    val message: ErrorMessenger = response.body()!!
                    toast(message.msg)
                } else {
                    H.l("Something wrong")
                }

            }

        })


    }

    private fun getImagePath(uri: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)!!
        cursor.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val mediaPath = cursor.getString(columnIndex)
        cursor.close()
        return mediaPath
    }
}
