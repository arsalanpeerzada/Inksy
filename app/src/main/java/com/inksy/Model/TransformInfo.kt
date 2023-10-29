package com.inksy.Model

import android.view.View

data class TransformInfo(

    var deltaX: Float = 0f,
    var deltaY: Float = 0f,
    var deltaScale: Float = 0f,
    var deltaAngle: Float = 0f,
    var pivotX: Float = 0f,
    var pivotY: Float = 0f,
    var minimumScale: Float = 0f,
    var maximumScale: Float = 0f,
    var viewTag: String? = null

)
