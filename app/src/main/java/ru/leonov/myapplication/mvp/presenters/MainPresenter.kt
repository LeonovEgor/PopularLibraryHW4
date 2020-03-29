package ru.leonov.myapplication.mvp.presenters

import io.reactivex.rxjava3.core.Scheduler
import ru.leonov.myapplication.mvp.model.IImageHelper
import ru.leonov.myapplication.mvp.model.entities.Image
import ru.leonov.myapplication.mvp.view.IMainView
import io.reactivex.rxjava3.schedulers.Schedulers


class MainPresenter (private val imageHelper: IImageHelper, private val view: IMainView, private val mainThreadScheduler: Scheduler) {

    private val quality = 100

    fun textChanged(value: String?) = value.let { view.updateText(it.toString())}

    fun showError(error: Throwable?) = error.let { view.showError(it.toString()) }

    fun loadImagePressed() = view.loadImage()

    fun onImageSelected(data: ByteArray) {
        val image = Image(data)
        imageHelper.convertImage(image, quality)
            .subscribeOn(Schedulers.io())
            .observeOn(mainThreadScheduler)
            .subscribe ({ pngImage ->
                view.showImage(pngImage)
            }, {
            view.showError(it.toString())
        })
    }

}