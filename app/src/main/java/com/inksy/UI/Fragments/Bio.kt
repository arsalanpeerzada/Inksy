package com.inksy.UI.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.inksy.Interfaces.OnDialogClickListener
import com.inksy.R
import com.inksy.UI.Activities.MainActivity
import com.inksy.UI.Dialogs.CameraGalleryDialog
import com.inksy.UI.ViewModel.LoginView
import com.inksy.Utils.FileUtil
import com.inksy.Utils.Permissions
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentBioBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.File

class Bio : Fragment() {

    lateinit var loginView: LoginView
    private lateinit var cameraUri: Uri
    private val PICK_REQUEST = 53
    lateinit var binding: FragmentBioBinding
    lateinit var tinyDB: TinyDB
    var isFromCam = false
    var token: String? = null
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        binding.circleImageView2.setImageURI(null)
        binding.circleImageView2.setImageURI(cameraUri)
        // startCrop(cameraUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentBioBinding.inflate(layoutInflater)
        tinyDB = TinyDB(requireContext())

        token = tinyDB.getString("token")
        cameraUri = Uri.parse("asdasdas")
        cameraUri = createImageUri()!!
        loginView = ViewModelProvider(requireActivity())[LoginView::class.java]
        loginView.init()
        binding.button.setOnClickListener() {
            if (binding.name1.text.isNullOrEmpty()) {
                binding.nameError.visibility = View.VISIBLE
            }
            if (binding.summary.text.isNullOrEmpty()) {
                binding.summary.error = getString(R.string.bioError)
            }

            if (!binding.name1.text.isNullOrEmpty() && !binding.summary.text.isNullOrEmpty()) {
                binding.loader.visibility = View.VISIBLE
                upload(token)
            }
        }

        binding.circleImageView2.setOnClickListener() {
            isFromCam = false
            onButtonShowPopupWindowClick()
        }

        return binding.root
    }

    private fun onButtonShowPopupWindowClick() {
        val cameraGalleryDialog =
            CameraGalleryDialog(requireContext(), object : OnDialogClickListener {

                override fun onDialogClick(callBack: String?) {
                    if (callBack == "Camera") {
                        if (!Permissions.Check_CAMERA(requireActivity())) {
                            Permissions.Request_CAMERA_STORAGE(requireActivity(), 11)
                        } else {
//                            val fileName = "" + System.currentTimeMillis()
//                            val values = ContentValues()
//                            values.put(MediaStore.Images.Media.TITLE, fileName)
//                            values.put(MediaStore.Images.Media.DESCRIPTION, "Camera")
//                            cameraUri = requireActivity().contentResolver.insert(
//                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                                values
//                            )!!
//                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
//                            intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//                            startActivityForResult(intent, CAMERA_REQUEST)

                            contract.launch(cameraUri)
                        }
                    } else if (callBack == "Gallery") {
                        if (!Permissions.Check_CAMERA(requireActivity())) {
                            Permissions.Request_CAMERA_STORAGE(requireActivity(), 11)
                        } else {
                            val intent = Intent()
                            intent.type = "image/*"
                            intent.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(
                                Intent.createChooser(intent, "Pick a Picture"),
                                53
                            )
                        }
                    }
                }
            })
        cameraGalleryDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        cameraGalleryDialog.show()
    }

    fun upload(token: String?) {
        var file: File = FileUtil.from(requireContext(), cameraUri)
        var isFromCam = false
        var requestFile: RequestBody

        try {
            if (isFromCam) {
                requestFile = RequestBody.create(
                    "jpg".toMediaTypeOrNull(),
                    FileUtil.from(requireContext(), cameraUri)
                )
            } else {
                if (requireContext().contentResolver.getType(cameraUri) != null) {
                    requestFile = RequestBody.create(
                        requireContext().contentResolver.getType(cameraUri)!!
                            .toMediaTypeOrNull(),
                        FileUtil.from(requireContext(), cameraUri)
                    )
                } else {
                    requestFile =
                        RequestBody.create(
                            ".png".toMediaTypeOrNull(),
                            FileUtil.from(requireContext(), cameraUri)
                        )
                }
                loginView.profile(
                    binding.name1.text.toString(),
                    binding.summary.text.toString(),
                    requestFile,
                    token!!
                )?.observe(requireActivity()) {
                    binding.loader.visibility = View.GONE
                    if (it?.status == 1) {

                        tinyDB.putString("fullname", it.data?.fullName)
                        tinyDB.putString("bio", it?.data?.bio)
                        tinyDB.putInt("isprofilecompleted", it.data?.isProfileCompleted!!)
                        tinyDB.putBoolean("isfirstTime", true)

                        if (it?.data?.avatar != null) {
                            tinyDB.putString("avatar", it?.data?.avatar!!)
                        }
                        if (it?.data?.avatar.isNullOrBlank()) {
                        } else {
                            tinyDB.putString("avatar", it?.data?.avatar)
                        }
                        requireContext().startActivity(
                            Intent(
                                requireContext(),
                                MainActivity::class.java
                            )
                        )

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                requireContext(),
                e.message.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_REQUEST) {
                cameraUri = data!!.data!!

                val uri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().applicationContext.contentResolver,
                    uri
                )
                Glide.with(requireContext()).load(bitmap).into(binding.circleImageView2)

            }
        }

    }

    private fun createImageUri(): Uri? {
        val image = File(requireActivity().applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(
            requireActivity().applicationContext,
            "com.inksy.fileprovider",
            image
        )
    }


}