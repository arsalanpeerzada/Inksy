package com.inksy.UI.Dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.inksy.R
import com.inksy.UI.Constants

class ImageViewDialog(
    var _context: Context,
    var image: String,
    var lockCheck: Boolean
) : Dialog(_context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.dialog_image)
        setCanceledOnTouchOutside(true)
        val window = window
        window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        var imageview = findViewById<ImageView>(R.id.image)
        var lock = findViewById<ImageView>(R.id.lock)

        if (lockCheck) {
            lock.visibility = View.VISIBLE
        } else {
            lock.visibility = View.GONE
        }


        Glide.with(_context).load(Constants.BASE_IMAGE + image).into(imageview)

    }
}