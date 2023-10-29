package com.inksy.Interfaces

import com.inksy.Model.Styles

interface OnDialogBulletClickListener {
    fun onDialogClick(
        callBacktv: Styles,
        callbackrv: ArrayList<Styles>,
        type: Int,
        layoutid: String
    )

}