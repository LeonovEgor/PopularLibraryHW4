package ru.leonov.myapplication.ui.converter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.leonov.myapplication.mvp.model.entities.Image
import ru.leonov.myapplication.mvp.model.images.IImageHelper
import java.io.File
import java.io.FileOutputStream

class ImageHelper(private val context: Context?) : IImageHelper {

    override fun savePngImage(image: Image, quality: Int): @NonNull Completable = Completable.fromAction {
        context?.let {cnt ->

            val bitmap = BitmapFactory.decodeByteArray(image.data, 0, image.data.size)
            val dst = File(cnt.getExternalFilesDir(null), "result.png")
            val stream = FileOutputStream(dst)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            bitmap.recycle()
            stream.close()
        }
    }.subscribeOn(Schedulers.io())

    fun toBitmap(image: Image): Bitmap = BitmapFactory
        .decodeByteArray(image.data, 0, image.data.size)
}