package ru.leonov.myapplication.mvp.model.images

import io.reactivex.rxjava3.core.Completable
import ru.leonov.myapplication.mvp.model.entities.Image

interface IImageHelper {
    fun savePngImage(image: Image, quality: Int): Completable
}