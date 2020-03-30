package ru.leonov.myapplication.mvp.presenters

import io.reactivex.rxjava3.core.Scheduler
import ru.leonov.myapplication.mvp.model.entities.Image
import ru.leonov.myapplication.mvp.view.IMainView


class MainPresenter (private val view: IMainView, private val mainThreadScheduler: Scheduler) {

    private val quality = 100

    fun textChanged(value: String?) = value.let { view.updateText(it.toString())}

    fun showError(error: Throwable?) = error.let { view.showError(it.toString()) }

    fun loadImagePressed() = view.loadImage()

    fun onImageSelected(image: Image) {
        view.savePng(image, quality)
    }

    fun onImageConverted(image: Image) {
        view.showImage(image)
    }

    fun onError(it: Throwable?) {
        view.showError(it.toString())
    }

}