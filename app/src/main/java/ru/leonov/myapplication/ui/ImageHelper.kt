package ru.leonov.myapplication.ui

import android.content.Context
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import ru.leonov.myapplication.mvp.model.IImageHelper
import ru.leonov.myapplication.mvp.model.entities.Image
import ru.leonov.myapplication.ui.utils.saveImageToInternalStorage

class ImageHelper : IImageHelper {

    override fun savePngImage(image: Image, quality: Int, context: Context): @NonNull Observable<Image> = Observable.fromCallable {
        return@fromCallable image.saveImageToInternalStorage(context, quality)
    }

}