package ru.leonov.myapplication.mvp.view

import ru.leonov.myapplication.mvp.model.entities.Image

interface IMainView {
    fun showError(error: String)
    fun updateText(text: String)
    fun loadImage()
    fun showImage(image: Image)
    fun savePng(image: Image, quality: Int)
}
