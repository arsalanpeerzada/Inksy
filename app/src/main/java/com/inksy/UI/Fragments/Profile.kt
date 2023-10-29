package com.inksy.UI.Fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.inksy.Database.JournalDatabase
import com.inksy.Interfaces.OnChangeStateClickListener
import com.inksy.Interfaces.OnDialogClickListener
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Model.Categories
import com.inksy.Model.Journals
import com.inksy.Model.UserModel
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Activities.ViewAll
import com.inksy.UI.Adapter.CategoriesAdapter
import com.inksy.UI.Adapter.FirstBookAdapter
import com.inksy.UI.Constants
import com.inksy.UI.Dialogs.CameraGalleryDialog
import com.inksy.UI.Dialogs.Comment_BottomSheet
import com.inksy.UI.ViewModel.EditProfileView
import com.inksy.UI.ViewModel.JournalView
import com.inksy.UI.ViewModel.PeopleView
import com.inksy.Utils.FileUtil
import com.inksy.Utils.Permissions
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentProfileBinding
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.UCropFragmentCallback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.Serializable


class Profile : Fragment(), iOnClickListerner, OnChangeStateClickListener, UCropFragmentCallback {
    private var fragment: UCropFragment? = null
    var list: ArrayList<Journals>? = ArrayList()
    var token: String = ""
    var isFromCam = false
    private val REQUEST_SELECT_PICTURE_FOR_FRAGMENT = 0x02
    private val SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage"
    lateinit var peopleView: PeopleView
    lateinit var editProfileView: EditProfileView
    var followersList: ArrayList<UserModel> = ArrayList()

    companion object {
        private val CAMERA_REQUEST = 1888
        private val GALLERY_REQUEST = 1
    }

    lateinit var journalDatabase: JournalDatabase

    //    private val requestMode = BuildConfig.RequestMode
    lateinit var tinydb: TinyDB
    lateinit var binding: FragmentProfileBinding
    private lateinit var cameraUri: Uri
    private val PICK_REQUEST = 53
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        binding.circleImageView.setImageURI(null)
        binding.circleImageView.setImageURI(cameraUri)
        upload()
        // startCrop(cameraUri)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        peopleView = ViewModelProvider(this)[PeopleView::class.java]
        peopleView.init()

        binding.loader.visibility = View.VISIBLE
        tinydb = TinyDB(requireContext())
        binding.name.text = tinydb.getString("fullname")
        binding.followpeople.text = "Followed by ${tinydb.getString("followers")} People"
        binding.points.text = tinydb.getString("points")
        binding.bio.text = tinydb.getString("bio")
        if (!tinydb.getString("avatar").isNullOrEmpty()) {
            Glide.with(requireContext()).load(Constants.BASE_IMAGE + tinydb.getString("avatar"))
                .placeholder(R.drawable.ic_empty_user)
                .into(binding.circleImageView)
        }
        token = tinydb.getString("token")!!
        val id = tinydb.getString("id")!!.toInt()
        getdetails(id, token)
        journalDatabase = JournalDatabase.getInstance(requireContext())!!
        editProfileView = ViewModelProvider(requireActivity())[EditProfileView::class.java]
        editProfileView.init()
        cameraUri = createImageUri()!!

        val newJournal = list
        var indexJournal = Journals(title = "@@@@")
//        newJournal?.add(0, indexJournal)
        binding.myJournal.adapter =
            FirstBookAdapter(
                requireContext(),
                newJournal!!,
                " ",
                object : iOnClickListerner {
                    override fun onclick(position: Int) {
                        Comment_BottomSheet(position).show(
                            childFragmentManager,
                            " "
                        )
                    }
                },
                this, journalDatabase
            )

//        binding.createJournal.setOnClickListener {
//
//
//            journalDatabase.getJournalData().DeleteData("1")
//            journalDatabase.getJournalData().deleteTable()
//            journalDatabase.getJournalData().deleteSelectedAudience()
//
//            requireContext().startActivity(
//                Intent(
//                    requireContext(),
//                    CreateActivity::class.java
//                )
//            )
//        }
        binding.followpeople.setOnClickListener {
            val intent = Intent(requireContext(), ViewAll::class.java).putExtra(
                "activity",
                Constants.peopleViewAll
            ).putExtra("List", followersList as Serializable).putExtra("Data", true)
            startActivity(intent)
        }

        binding.seeall1.setOnClickListener {
            list?.removeAt(0)
            requireContext().startActivity(
                Intent(requireContext(), ViewAll::class.java).putExtra(
                    Constants.activity,
                    Constants.sub_journalViewAll
                ).putExtra("Data", true)
                    .putExtra("List", list as Serializable)
            )

        }
        binding.write.setOnClickListener {
            val action = ProfileDirections.actionProfileToEditProfile()
            findNavController().navigate(action)
        }
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
            requireActivity().finish()
        }


        binding.camera.setOnClickListener {
            isFromCam = false
            onButtonShowPopupWindowClick()

        }


//        binding.followpeople.setOnClickListener() {
//            var intent = Intent(requireContext(), ViewAll::class.java).putExtra(
//                "activity", Constants.peopleViewAll
//            )
//            requireContext().startActivity(intent)
//        }

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
                                1
                            )
                        }
                    }
                }
            })
        cameraGalleryDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        cameraGalleryDialog.show()
    }

    fun getdetails(id: Int, token: String) {
        peopleView.userDetail(id, token)?.observe(requireActivity()) { it ->
            binding.loader.visibility = View.GONE
            when (it.status) {
                Status.ERROR -> {

                }
                Status.SUCCESS -> {
//                    Toast.makeText(this, it.data?.data?.fullName, Toast.LENGTH_SHORT).show()

                    binding.followpeople.text =
                        "Followed by ${it.data?.data?.followerCount} People"

                    followersList = it.data?.data?.followers!!

                    binding.points.text = it.data.data?.points.toString()
                    list = it.data.data?.journals
                    val newJournal = list
                    var indexJournal = Journals(title = "@@@@")
                    newJournal?.add(0, indexJournal)

                    binding.myJournal.adapter =
                        FirstBookAdapter(
                            requireContext(),
                            newJournal!!,
                            " ",
                            object : iOnClickListerner {
                                override fun onclick(position: Int) {
                                    Comment_BottomSheet(position).show(
                                        childFragmentManager,
                                        " "
                                    )
                                }
                            },
                            this, journalDatabase
                        )

//                    binding.myJournal.adapter?.notifyDataSetChanged()

                    if (list?.size == 0) {
//                        binding.textView3.visibility = View.GONE
//                        binding.textView4.visibility = View.GONE
                        binding.seeall1.visibility = View.GONE

                    } else {
                        binding.textView3.visibility = View.VISIBLE
                        binding.textView4.visibility = View.VISIBLE
                        binding.myJournal.visibility = View.VISIBLE
                        binding.seeall1.visibility = View.VISIBLE


                        val categoryList = ArrayList<Categories>()
                        val listString = tinydb.getListString("categoriesList")

                        for (i in 0 until listString.size) {
                            val string = listString[i]
                            val gson = Gson()
                            categoryList.add(gson.fromJson(string, Categories::class.java))

                        }



                        binding.categories.adapter =
                            CategoriesAdapter(
                                requireContext(),
                                categoryList,
                                list!!,
                                "",
                                object : iOnClickListerner {
                                    override fun onclick(position: Int) {
                                        Comment_BottomSheet(position).show(
                                            childFragmentManager,
                                            " "
                                        )
                                    }
                                },
                                this
                            )

                    }
                }
                Status.LOADING -> {

                }
            }

            //it.data?.data?.fullName
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                if (data?.data != null) {
                    val bitmap = data.extras?.get("data")
                    Glide.with(this).load(bitmap).into(binding.circleImageView)
                } else {

                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver, data?.data
                    )
                    Glide.with(this).load(bitmap).into(binding.circleImageView)
                }

            } else if (requestCode == GALLERY_REQUEST) {
//                if (requestCode == requestMode) {
                cameraUri = data!!.data!!

                val uri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().applicationContext.contentResolver,
                    uri
                )

                Glide.with(this).load(cameraUri).into(binding.circleImageView)
                // cameraUri = resultUri
                upload()
//                    startCrop(uri!!)

            } else if (requestCode == UCrop.REQUEST_CROP) {
                //  handleCropResult(data!!)
            }

            //upload()

            // }
        }

    }

    private fun handleCropResult(result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
//            Glide.with(this).load(resultUri).into(binding.circleImageView)
//            cameraUri = resultUri
//            upload()
        } else {
            Toast.makeText(
                requireContext(),
                "Cannot retrieve cropped image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onStateChange(position: Int, like: Boolean, type: String) {
        super.onStateChange(position, like, type)

        if (like) {
            likeJournal(position, like)
        } else {
            likeJournal(position, like)
        }
    }

    private fun likeJournal(id: Int?, like: Boolean) {
        val journalView: JournalView =
            ViewModelProvider(requireActivity())[JournalView::class.java]
        journalView.init()
        journalView.journalLike(
            id.toString(),
            token
        )?.observe(requireActivity()) {

            if (it?.data?.status == 1) {
                Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(requireContext(), it?.data?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun upload() {
        val requestFile: RequestBody

        if (cameraUri != null) {
            try {
                if (isFromCam) {
                    requestFile = FileUtil.from(requireContext(), cameraUri)
                        .asRequestBody("jpg".toMediaTypeOrNull())
                } else {
                    if (requireContext().contentResolver.getType(cameraUri) != null) {
                        requestFile = FileUtil.from(requireContext(), cameraUri)
                            .asRequestBody(
                                requireContext().contentResolver.getType(cameraUri)!!
                                    .toMediaTypeOrNull()
                            )
                    } else {
                        requestFile =
                            FileUtil.from(requireContext(), cameraUri)
                                .asRequestBody(".png".toMediaTypeOrNull())
                    }
                }

                val name = tinydb.getString("fullname")
                val bio = tinydb.getString("bio")
                val token = tinydb.getString("token")


                editProfileView.profile(
                    name!!,
                    bio!!,
                    requestFile,
                    token!!
                )?.observe(requireActivity()) {
                    binding.loader.visibility = View.GONE
                    if (it?.status == 1) {

                        tinydb.putString("avatar", it.data?.avatar)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                            .show()

                    } else {
                        Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT)
                            .show()
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
    }

    private fun startCrop(uri: Uri) {
        val destinationFileName: String = SAMPLE_CROPPED_IMAGE_NAME

        var uCrop = UCrop.of(uri, Uri.fromFile(File(activity?.cacheDir, destinationFileName)))
        uCrop = basisConfig(uCrop)
        uCrop = advancedConfig(uCrop)
        // else start uCrop Activity
        uCrop.start(requireActivity())

    }

    private val mEditTextRatioX: EditText? = null
    private var mEditTextRatioY: EditText? = null
    private fun basisConfig(uCrop: UCrop): UCrop? {
        var uCrop = uCrop
        try {
            val ratioX: Float =
                java.lang.Float.valueOf(mEditTextRatioX?.text.toString().trim { it <= ' ' })
            val ratioY: Float =
                java.lang.Float.valueOf(mEditTextRatioY?.text.toString().trim { it <= ' ' })
            if (ratioX > 0 && ratioY > 0) {
                uCrop = uCrop.withAspectRatio(ratioX, ratioY)
            }
        } catch (e: NumberFormatException) {
//            Log.i(
//                com.yalantis.ucrop.sample.SampleActivity.TAG,
//                String.format("Number please: %s", e.message)
//            )
        }


        return uCrop
    }

    private fun advancedConfig(uCrop: UCrop): UCrop? {
        val options = UCrop.Options()

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)


        /*
        If you want to configure how gestures work for all UCropActivity tabs

        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        * */

        /*
        This sets max size for bitmap that will be decoded from source Uri.
        More size - more memory allocation, default implementation uses screen diagonal.

        options.setMaxBitmapSize(640);
        * */


        /*

        Tune everything (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧

        options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(666);
        options.setDimmedLayerColor(Color.CYAN);
        options.setCircleDimmedLayer(true);
        options.setShowCropFrame(false);
        options.setCropGridStrokeWidth(20);
        options.setCropGridColor(Color.GREEN);
        options.setCropGridColumnCount(2);
        options.setCropGridRowCount(1);
        options.setToolbarCropDrawable(R.drawable.your_crop_icon);
        options.setToolbarCancelDrawable(R.drawable.your_cancel_icon);

        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setActiveControlsWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));

        // Aspect ratio options
        options.setAspectRatioOptions(2,
            new AspectRatio("WOW", 1, 2),
            new AspectRatio("MUCH", 3, 4),
            new AspectRatio("RATIO", CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO),
            new AspectRatio("SO", 16, 9),
            new AspectRatio("ASPECT", 1, 1));
        options.withAspectRatio(CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO);
        options.useSourceImageAspectRatio();

       */return uCrop.withOptions(options)
    }

    private fun createImageUri(): Uri? {
        val image = File(requireActivity().applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(
            requireActivity().applicationContext,
            "com.inksy.fileprovider",
            image
        )
    }

    override fun loadingProgress(showLoader: Boolean) {

    }

    override fun onCropFinish(result: UCropFragment.UCropResult?) {
        when (result!!.mResultCode) {
            RESULT_OK -> handleCropResult(result.mResultData)
            UCrop.RESULT_ERROR -> handleCropError(result.mResultData)
        }
    }

    private fun handleCropError(result: Intent) {
        val cropError = UCrop.getError(result)
        if (cropError != null) {
            Log.e("TAG", "handleCropError: ", cropError)
            Toast.makeText(requireContext(), cropError.message, Toast.LENGTH_LONG).show()
        } else {

            Toast.makeText(requireContext(), "Unexpected error", Toast.LENGTH_SHORT)
                .show()
        }
    }
}