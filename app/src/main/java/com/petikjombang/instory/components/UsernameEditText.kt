package com.petikjombang.instory.components

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class UsernameEditText : AppCompatEditText {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Masukkan nama lengkap"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }
}