package com.inksy.UI.Activities

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.inksy.UI.Dialogs.CameraGalleryDialog
import com.inksy.Interfaces.OnDialogClickListener
import com.inksy.R
import com.inksy.Utils.Permissions
import com.inksy.databinding.ActivityChatBinding


class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding
    var cameraUri: Uri? = null
    var imagePath: String? = null

    companion object {
        private val CAMERA_REQUEST = 1888
        private val GALLERY_REQUEST = 100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivBack.setOnClickListener {
            onBackPressed()
            this.finish()
        }

        binding.imgAttach.setOnClickListener {
            onButtonShowPopupWindowClick()
        }
    }

    private fun onButtonShowPopupWindowClick() {

            val cameraGalleryDialog =
                CameraGalleryDialog(this, object : OnDialogClickListener {

                    override fun onDialogClick(callBack: String?) {
                        if (callBack == "Camera") {
                            if (!Permissions.Check_CAMERA(this@ChatActivity) || !Permissions.Check_STORAGE(
                                    this@ChatActivity
                                )
                            ) {
                                Permissions.Request_CAMERA_STORAGE(this@ChatActivity, 11)
                            } else {
                                val fileName = "" + System.currentTimeMillis()
                                val values = ContentValues()
                                values.put(MediaStore.Images.Media.TITLE, fileName)
                                values.put(MediaStore.Images.Media.DESCRIPTION, "Camera")
                                cameraUri = contentResolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values
                                )
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
                                intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                startActivityForResult(intent, ChatActivity.CAMERA_REQUEST)
                            }
                        } else if (callBack == "Gallery") {
                            if (!Permissions.Check_STORAGE(this@ChatActivity)) {
                                Permissions.Request_STORAGE(this@ChatActivity, 22)
                            } else {
                                val intent = Intent()
                                intent.type = "image/*"
                                intent.action = Intent.ACTION_GET_CONTENT
                                startActivityForResult(
                                    Intent.createChooser(intent, "Pick a Picture"),
                                    ChatActivity.GALLERY_REQUEST
                                )
                            }
                        }
                    }
                })
            cameraGalleryDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            cameraGalleryDialog.show()
    }

    fun open(){

        val builder = AlertDialog.Builder(this)

        val inflater = layoutInflater
        val dialogView: View = inflater.inflate(com.inksy.R.layout.dialog, null)

        builder.setView(dialogView)



        val dialog = builder.create()


        // Display the custom alert dialog on interface

        // Display the custom alert dialog on interface
        dialog.show()
    }
}