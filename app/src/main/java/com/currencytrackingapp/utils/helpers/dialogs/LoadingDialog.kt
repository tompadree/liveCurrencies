package com.currencytrackingapp.utils.helpers.dialogs


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import com.currencytrackingapp.R
import pl.droidsonroids.gif.GifImageView

class LoadingDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(createDialogView())
    }

    private fun createDialogView(): View {
        val rootView = FrameLayout(context)
        rootView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        rootView.setBackgroundResource(android.R.color.transparent)
        rootView.isClickable = true
        rootView.isFocusable = true
        rootView.setOnClickListener {  }

        val progressView = GifImageView(context)
        progressView.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
        progressView.setImageResource(R.drawable.loader)

        rootView.addView(progressView)

        return rootView
    }

}