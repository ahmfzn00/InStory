package com.petikjombang.instory.components

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class PassEditText: AppCompatEditText {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Masukkan password Anda"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    error = "Password harus terdiri dari 8 karakter"
                } else {

                }
            }
            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
}