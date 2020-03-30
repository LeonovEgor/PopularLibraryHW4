package ru.leonov.myapplication.mvp.model

import android.content.Context
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import ru.leonov.myapplication.mvp.model.entities.Image

interface IImageHelper {

    fun savePngImage(image: Image, quality: Int, context: Context): @NonNull Observable<Image>
}