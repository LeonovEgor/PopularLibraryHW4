package ru.leonov.myapplication.ui.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ru.leonov.myapplication.mvp.model.entities.Image
import java.io.ByteArrayOutputStream

fun convertToPng(image: Image, quality: Int): Image {

    val bitmap = BitmapFactory.decodeByteArray(image.data, 0, image.data.size)
    val stream = ByteArrayOutputStream()
    val res = bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream)
    if (!res) throw Exception("Conversion error.")

    return Image(stream.toByteArray())
}

fun imageToBitmap(image: Image) = BitmapFactory
    .decodeByteArray(image.data, 0, image.data.size)

