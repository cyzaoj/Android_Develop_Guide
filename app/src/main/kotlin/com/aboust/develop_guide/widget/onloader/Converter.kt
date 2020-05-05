package com.aboust.develop_guide.widget.onloader

import com.aboust.develop_guide.widget.onloader.callback.Callback


interface Converter<T> {
    fun map(t: T): Class<out Callback?>
}
