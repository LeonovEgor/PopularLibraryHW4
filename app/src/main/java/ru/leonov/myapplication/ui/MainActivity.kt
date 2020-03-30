@file:JvmName("RxTextView")
@file:JvmMultifileClass

package ru.leonov.myapplication.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import ru.leonov.myapplication.R
import ru.leonov.myapplication.mvp.model.entities.Image
import ru.leonov.myapplication.mvp.presenters.MainPresenter
import ru.leonov.myapplication.mvp.view.IMainView
import ru.leonov.myapplication.ui.utils.toBitmap
import rx.Subscription

class MainActivity : AppCompatActivity(), IMainView {

    private lateinit var presenter: MainPresenter
    private lateinit var editTextSub: Subscription
    private lateinit var btnImageSub: Subscription

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, AndroidSchedulers.mainThread())
    }

    override fun onStart() {
        super.onStart()

        initTextEdit()
        initImageConverting()
    }

    private fun initTextEdit() {
        editTextSub = RxTextView.textChanges(ti_edit)
            .subscribe({
                presenter.textChanged(it.toString())
            }, {
                presenter.showError(it)
            })
    }

    private fun initImageConverting() {
        btnImageSub = RxView.clicks(btn_load_image).subscribe({
            presenter.loadImagePressed()
        }, {
            presenter.showError(it)
        })
    }

    override fun onStop() {
        editTextSub.unsubscribe()
        btnImageSub.unsubscribe()

        super.onStop()
    }

    override fun showError(error: String) {
        textView.text = error
    }

    override fun updateText(text: String) {
        textView.text = text
    }

    override fun loadImage() {
        selectImageInAlbum()
    }

    override fun showImage(image: Image) {
        image_view.setImageBitmap(image.toBitmap())
    }

    override fun savePng(image: Image, quality: Int) {
        ImageHelper().savePngImage(image, quality, this)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ pngImage ->
                presenter.onImageConverted(pngImage)
            }, {
                presenter.onError(it)
            })
    }

    private fun selectImageInAlbum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
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
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,
            IMAGE_PICK_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            data?.let {
                it.data?.let {uri ->

                    val imageStream = contentResolver.openInputStream(uri)
                    imageStream?.let { stream ->
                        presenter.onImageSelected(Image(stream.readBytes()))
                    }
                }
            }
        }
    }

}
