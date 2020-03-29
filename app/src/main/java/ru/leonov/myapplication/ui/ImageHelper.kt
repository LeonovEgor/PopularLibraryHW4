package ru.leonov.myapplication.ui

import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import ru.leonov.myapplication.mvp.model.IImageHelper
import ru.leonov.myapplication.mvp.model.entities.Image
import ru.leonov.myapplication.ui.converter.convertToPng

class ImageHelper : IImageHelper {

    override fun convertImage(image: Image, quality: Int): @NonNull Observable<Image> = Observable.fromCallable {
        return@fromCallable convertToPng(image, quality)
    }

    override fun saveImage(image: Image) = Observable.fromCallable<Image> {
        TODO("Not yet implemented")
    }

}