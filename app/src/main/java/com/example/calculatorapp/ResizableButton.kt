package com.example.calculatorapp

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet


class ResizableButton(private val c : Context, private var attrs : AttributeSet) :
    androidx.appcompat.widget.AppCompatButton(c, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val height = MeasureSpec.getSize(heightMeasureSpec)
            setMeasuredDimension(height, height)
        } else {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            setMeasuredDimension(width, width)
        }
    }
}
