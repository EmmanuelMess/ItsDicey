package com.emmanuelmess.itsdicey

import android.content.Context
import android.util.DisplayMetrics

val Int.px: Pixels
    get() = Pixels(this.toFloat())

val Int.dp: DensityPixels
    get() = DensityPixels(this.toFloat())

inline class Pixels(val value: Float) {
    fun toInt() = value.toInt()
    fun toDp(context: Context) = DensityPixels(value / (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT))
}

inline class DensityPixels(val value: Float) {
    fun toInt() = value.toInt()
    fun toPx(context: Context) = Pixels(value * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT))
}


