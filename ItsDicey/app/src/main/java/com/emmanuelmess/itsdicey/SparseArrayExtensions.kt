package com.emmanuelmess.itsdicey

import android.util.SparseArray

inline fun <T> SparseArray<T>.forEachIndexed(action: (index: Int, T) -> Unit): Unit {
    for(i in 0 until size()) {
        val element = valueAt(i) ?: continue

        action(i, element)
    }
}