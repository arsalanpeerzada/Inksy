package com.inksy.Interfaces

import android.view.View
import com.inksy.Model.Styles

interface PopUpOnClickListerner {

    fun popuponclick(data: String, itemView: View, list: ArrayList<Styles>, position: Int) {}


}