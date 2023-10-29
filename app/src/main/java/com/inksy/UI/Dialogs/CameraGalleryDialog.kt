package com.inksy.UI.Dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import com.inksy.Interfaces.OnDialogClickListener
import com.inksy.R

class CameraGalleryDialog(context: Context, var listener: OnDialogClickListener) : Dialog(context) {

    private lateinit var cameraLayout: LinearLayout
    private lateinit var galleryLayout:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog)
        setCanceledOnTouchOutside(true)
        val window = window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        cameraLayout = findViewById(R.id.cameraLayout)
        galleryLayout = findViewById(R.id.galleryLayout)
        cameraLayout.setOnClickListener(View.OnClickListener {
            listener.onDialogClick("Camera")
            dismiss()

          //  Toast.makeText(context, "Feature coming soon", Toast.LENGTH_SHORT).show()
        })
        galleryLayout.setOnClickListener(View.OnClickListener {
            listener.onDialogClick("Gallery")
            dismiss()
        })
    }
}