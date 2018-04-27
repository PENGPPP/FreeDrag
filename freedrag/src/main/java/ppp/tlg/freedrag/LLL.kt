package com.mw.mediawatcher

import android.util.Log

fun info(vararg v: Any?) {

    var log = ""
    v.forEach {
        log = "$log $it"
    }
    Log.i("LLL_LLL_TAG", log)
}
