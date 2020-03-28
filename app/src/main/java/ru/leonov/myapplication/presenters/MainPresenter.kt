package ru.leonov.myapplication.presenters

import ru.leonov.myapplication.view.IMainView

class MainPresenter (private val view: IMainView) {

    fun textChanged(value: String?) = value.let { view.updateText(it.toString())}

    fun showError(error: Throwable?) = error.let { view.showError(it.toString()) }

}