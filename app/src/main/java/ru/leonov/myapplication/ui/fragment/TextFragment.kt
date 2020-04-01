@file:JvmName("RxTextView")
@file:JvmMultifileClass

package ru.leonov.myapplication.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.fragment_text.*

import ru.leonov.myapplication.R
import ru.leonov.myapplication.mvp.presenters.TextPresenter
import ru.leonov.myapplication.mvp.view.ITextView
import rx.Subscription

class TextFragment : Fragment(), ITextView {

    private lateinit var presenter: TextPresenter
    private lateinit var editTextSub: Subscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = TextPresenter(this)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        return inflater.inflate(R.layout.fragment_text, container, false)
    }

    override fun onStart() {
        super.onStart()

        initTextEdit()
    }

    private fun initTextEdit() {
        editTextSub = RxTextView.textChanges(ti_edit)
            .subscribe({
                presenter.textChanged(it.toString())
            }, {
                presenter.showError(it)
            })
    }

    override fun onStop() {
        editTextSub.unsubscribe()

        super.onStop()
    }

    override fun updateText(text: String) {
        textView.text = text
    }

    override fun showError(error: String) {
        textView.text = error
    }
}