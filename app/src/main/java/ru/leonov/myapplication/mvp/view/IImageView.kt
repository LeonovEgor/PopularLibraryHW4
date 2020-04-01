package ru.leonov.myapplication.mvp.view

import ru.leonov.myapplication.mvp.model.entities.Image

interface IImageView {
    fun showConvertInProgress()
    fun hideConvertInProgress()

    fun showError(error: String)
    fun loadImage()
    fun showImage(image: Image)
    fun showCancelMessage()
}
