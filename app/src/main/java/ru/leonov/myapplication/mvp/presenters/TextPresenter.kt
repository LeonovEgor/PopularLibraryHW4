package ru.leonov.myapplication.mvp.presenters

import ru.leonov.myapplication.mvp.view.ITextView

class TextPresenter (private val view: ITextView) {

    fun textChanged(value: String?) = value.let { view.updateText(it.toString())}
    fun showError(error: Throwable?) = error.let { view.showError(it.toString()) }

}