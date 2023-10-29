package com.inksy.Model

import android.graphics.Paint
import android.graphics.Path

data class Shape_BrushModel(
    var paint: Paint? = null,
    var shape: Shape? = null,
) {

    data class Shape(
        var top: Double? = null,
        var bottom: Double? = null,
        var left: Double? = null,
        var right: Double? = null,
        var tag: String? = null,
        var path: Path? = null
    ) {
    }
}

