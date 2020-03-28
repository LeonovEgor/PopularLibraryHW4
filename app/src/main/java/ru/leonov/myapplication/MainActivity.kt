@file:JvmName("RxTextView")
@file:JvmMultifileClass

package ru.leonov.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.activity_main.*
import ru.leonov.myapplication.presenters.MainPresenter
import ru.leonov.myapplication.view.IMainView
import rx.Subscription


class MainActivity : AppCompatActivity(), IMainView {

    private lateinit var presenter: MainPresenter
    private lateinit var editTextSub: Subscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this)

        initView()
    }

    private fun initView() {
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

    override fun showError(error: String) {
        textView.text = error
    }

    override fun updateText(text: String) {
        textView.text = text
    }

}
