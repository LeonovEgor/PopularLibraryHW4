package ru.leonov.myapplication.mvp.model

import io.reactivex.rxjava3.core.Observable
import ru.leonov.myapplication.mvp.model.entities.Image

interface IImageHelper {

    fun convertImage(image: Image, quality: Int) : Observable<Image>
    fun saveImage(image: Image) : Observable<Image>
}