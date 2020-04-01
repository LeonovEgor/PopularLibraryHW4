package ru.leonov.myapplication.mvp.presenters

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import ru.leonov.myapplication.mvp.model.entities.Image
import ru.leonov.myapplication.mvp.view.IImageView
import ru.leonov.myapplication.ui.converter.ImageHelper

class ImagePresenter(private val mainThreadScheduler: Scheduler,
                     private val view: IImageView,
                     private val imageHelper: ImageHelper) {

    private val quality = 100
    var convertDisposable: Disposable? = null

    fun loadImagePressed() = view.loadImage()

    fun imageSelected(image: Image) {
        view.showConvertInProgress()
        convertDisposable = imageHelper.savePngImage(image, quality)
            .observeOn(mainThreadScheduler)
            .subscribe ({
                view.hideConvertInProgress()
                view.showImage(image)
            }, {
                view.hideConvertInProgress()
                view.showError(it.toString())
            })
    }

    fun cancelClick() {
        convertDisposable?.dispose()
        view.hideConvertInProgress()
        view.showCancelMessage()
    }

    fun onError(it: Throwable?) = it.let { view.showError(it.toString()) }
}
