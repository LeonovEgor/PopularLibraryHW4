package ru.leonov.myapplication.ui.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ru.leonov.myapplication.mvp.model.entities.Image
import java.io.*
import java.util.*

fun Bitmap.toImage(quality: Int): Image {
    val stream = ByteArrayOutputStream()
    val res = this.compress(Bitmap.CompressFormat.PNG, quality, stream)
    if (!res) throw Exception("Conversion error.")
    return Image(stream.toByteArray())
}

fun Image.saveImageToInternalStorage(context: Context, quality: Int): Image {

    val bitmap = this.toBitmap()

    val wrapper = ContextWrapper(context)
    var file = wrapper.getDir("images", Context.MODE_PRIVATE)
    file = File(file, "${UUID.randomUUID()}.png")

    try {
        val stream: OutputStream = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream)
        stream.flush()
        stream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return bitmap.toImage(quality)
}

fun Image.toBitmap(): Bitmap = BitmapFactory
    .decodeByteArray(this.data, 0, this.data.size)
