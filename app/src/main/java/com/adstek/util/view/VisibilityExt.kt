package com.adstek.util.view

import android.view.View

fun View.showView() {
    this.visibility = View.VISIBLE
}

fun List<View>.showViews() {
    forEach {
        it.visibility = View.VISIBLE
    }
}


fun View.removeViews(vararg views: View) {
    views.forEach {
        it.visibility = View.GONE
    }
}


fun View.hideView() {
    this.visibility = View.INVISIBLE
}
fun View.removeView() {
    this.visibility = View.GONE
}