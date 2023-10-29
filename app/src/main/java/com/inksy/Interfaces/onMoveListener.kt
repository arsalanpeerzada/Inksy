package com.inksy.Interfaces

import android.view.View
import com.inksy.Model.TransformInfo

interface onMoveListener {
    fun onMove(view: View, info: TransformInfo){}
}