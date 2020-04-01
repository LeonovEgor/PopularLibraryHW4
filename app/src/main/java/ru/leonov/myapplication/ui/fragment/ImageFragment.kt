@file:JvmName("RxTextView")
@file:JvmMultifileClass

package ru.leonov.myapplication.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding.view.RxView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_image.*
import ru.leonov.myapplication.R
import ru.leonov.myapplication.mvp.model.entities.Image
import ru.leonov.myapplication.mvp.presenters.ImagePresenter
import ru.leonov.myapplication.mvp.view.IImageView
import ru.leonov.myapplication.ui.converter.ImageHelper
import rx.Subscription

class ImageFragment : Fragment(), IImageView {

    private lateinit var presenter: ImagePresenter
    private lateinit var imageHelper: ImageHelper

    private lateinit var btnImageSub: Subscription

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageHelper = ImageHelper(context)
        presenter = ImagePresenter(AndroidSchedulers.mainThread(),this, imageHelper)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onStart() {
        super.onStart()

        initImageConverting()
    }

    private fun initImageConverting() {
        btnImageSub = RxView.clicks(btn_load_image).subscribe({
            presenter.loadImagePressed()
        }, {
            presenter.onError(it)
        })
    }

    override fun onStop() {
        btnImageSub.unsubscribe()

        super.onStop()
    }

    override fun showError(error: String) {
        textView.text = error
    }

    override fun loadImage() {
        selectImageInAlbum()
    }

    //region permissions and image select

    private fun selectImageInAlbum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions,
                    PERMISSION_CODE
                )
            } else {
                pickImageFromGallery()
            }
        } else {
            pickImageFromGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(context, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            data?.data?.let { uri ->
                val bytes = context?.contentResolver?.openInputStream(uri)?.buffered()?.use { it.readBytes() }
                bytes?.let {
                    presenter.imageSelected(Image(bytes))
                }
            }
        }
    }

    //endregion

    var convertInProgressDialog: Dialog? = null

    override fun showConvertInProgress() {
        convertInProgressDialog = AlertDialog.Builder(context)
            .setMessage("Изображение конвертируется...")
            .setNegativeButton("Отмена") {dialog, which ->  presenter.cancelClick() }
            .create()
        convertInProgressDialog?.show()
    }

    override fun hideConvertInProgress() {
        convertInProgressDialog?.dismiss()
    }

    override fun showImage(image: Image) {
        image_view.setImageBitmap(imageHelper.toBitmap(image))
    }

    override fun showCancelMessage() {
        Toast.makeText(context, "Конвертирование отменено", Toast.LENGTH_SHORT).show()
    }
}
