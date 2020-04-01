package ru.leonov.myapplication.mvp.view

interface ITextView {
    fun updateText(text: String)
    fun showError(error: String)
}
