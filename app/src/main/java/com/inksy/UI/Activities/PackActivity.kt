package com.inksy.UI.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.inksy.Interfaces.OnDialogClickListener
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.AddDoodleAdapter
import com.inksy.UI.Dialogs.CameraGalleryDialog
import com.inksy.UI.ViewModel.DoodleView
import com.inksy.Utils.FileUtil
import com.inksy.Utils.Permissions
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityPackBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.File

class PackActivity : AppCompatActivity(), iOnClickListerner {
    private val PICK_IMAGE_BACKGROUND = 200
    private val CAMERA_REQUEST = 52
    private val PICK_IMAGE_LIST = 53
    private val PICK_DOODLE_LIST = 100
    lateinit var doodleView: DoodleView
    lateinit var binding: ActivityPackBinding
    lateinit var tinydb: TinyDB
    var token = ""
    lateinit var cameraUri: Uri
    var coverInitialized = false
    var list: ArrayList<Uri> = ArrayList()
    var position = 0
    var cameraSelected = 0
    lateinit var adapter: AddDoodleAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraUri = Uri.parse("0")
        tinydb = TinyDB(this)
        token = tinydb.getString("token").toString()
        doodleView = ViewModelProvider(this)[DoodleView::class.java]
        doodleView.init()
        var pricerange = tinydb.getString("pricerange")
        var data = pricerange?.split("-")
        var pricemin = "0.9"
        var pricemax = "4.9"

        binding.slider.valueFrom = pricemin.toFloat()!!
        binding.slider.valueTo = pricemax.toFloat()!!
        var average = (pricemax.toFloat() + pricemin.toFloat()) / 2
        binding.slider.value = average
        binding.slider.stepSize = 1F
        binding.price.text = "$ $pricemin - $ $pricemax"
        binding.price2.text = "Pack of : $ $average"

        var i = intent.getBooleanExtra("fromAdapter", false)

        if (i) {
            binding.slider.visibility = View.GONE
            binding.editTitle.visibility = View.GONE
            binding.sliderleft.visibility = View.GONE
            binding.sliderright.visibility = View.GONE
            binding.buyNow.visibility = View.VISIBLE
            binding.price.visibility = View.GONE
            binding.checked.visibility = View.GONE
            binding.price.text = "Pack Price : $2.50"

        } else {
            binding.slider.visibility = View.VISIBLE
            binding.editTitle.visibility = View.VISIBLE
            binding.sliderleft.visibility = View.VISIBLE
            binding.sliderright.visibility = View.VISIBLE
            binding.buyNow.visibility = View.GONE
            binding.price.visibility = View.VISIBLE
            binding.checked.visibility = View.VISIBLE
        }

        //  binding.rvPackTitle.adapter = PackAdapter(this@PackActivity)

        binding.editTitle.setOnClickListener {
            binding.title.isEnabled = !binding.title.isEnabled
            binding.title.setText("    ")
            binding.title.requestFocus()
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
            this.finish()
        }
        binding.checked.setOnClickListener {

            binding.checked.isEnabled = false
            var file: File = FileUtil.from(this, cameraUri)
            var price = binding.slider.value.toString() + "9"

            if (list.size > 0) {
                upload(token, binding.title.text.toString(), price, file)
            } else {
                Toast.makeText(this, "Upload atleast one doodle", Toast.LENGTH_SHORT).show()
            }


        }
        binding.buyNow.setOnClickListener {

            this.finish()
        }
        binding.slider.addOnChangeListener { slider, value, fromUser ->
            val number2digits: Double = String.format("%.2f", value).toDouble()
            binding.price2.text = "Your Price is : $ ${number2digits}"
        }

        binding.imageView14.setOnClickListener {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), PICK_IMAGE_BACKGROUND
            )
        }

        binding.image1.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 1
            )
        }
        binding.image2.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 2
            )
        }
        binding.image3.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 3
            )
        }
        binding.image4.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 4
            )
        }
        binding.image5.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 5
            )
        }
        binding.image6.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 6
            )
        }
        binding.image7.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 7
            )
        }
        binding.image8.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 8
            )
        }
        binding.image9.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 9
            )
        }
        binding.image10.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 10
            )
        }
        binding.image11.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 11
            )
        }
        binding.image12.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 12
            )
        }
        binding.image13.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 13
            )
        }
        binding.image14.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 14
            )
        }
        binding.image15.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 15
            )
        }
        binding.image16.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 16
            )
        }
        binding.image17.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 17
            )
        }
        binding.image18.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 18
            )
        }
        binding.image19.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 19
            )
        }
        binding.image20.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 20
            )
        }
        binding.image21.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 21
            )
        }
        binding.image22.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 22
            )
        }
        binding.image23.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 23
            )
        }
        binding.image24.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 24
            )
        }
        binding.image25.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 25
            )
        }
        binding.image26.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 26
            )
        }
        binding.image27.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 27
            )
        }
        binding.image28.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 28
            )
        }
        binding.image29.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 29
            )
        }
        binding.image30.setOnClickListener() {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), 30
            )
        }
    }

    fun upload(token: String, packTitle: String, packPrice: String, file: File) {

        doodleView.createPack(token, packTitle, packPrice, file)?.observe(this) {
            when (it?.status) {
                Status.LOADING -> {}
                Status.ERROR -> {

                }
                Status.SUCCESS -> {

                    var it = it?.data?.data


                    addDoodle(it?.id!!)
                }

                else -> {}
            }
        }
    }

    private fun addDoodle(id: Int) {

        if (list.size > 0) {
            for (i in 0 until list.size) {
                var file: File = FileUtil.from(this, list[i])

                val doodlePackId: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), id.toString())
                var cover_image: RequestBody
                cover_image = RequestBody.create(".png".toMediaTypeOrNull(), file)

                doodleView.addDoodle(doodlePackId, cover_image, token)?.observe(this) {
                    when (it?.status) {
                        Status.LOADING -> {}
                        Status.ERROR -> {
                            binding.checked.isEnabled = true
                        }
                        Status.SUCCESS -> {
                            binding.checked.isEnabled = true
                            this.finish()
                            Toast.makeText(this, it?.data?.message, Toast.LENGTH_SHORT).show()
                        }

                        else -> {}
                    }
                }
            }
        }

        this.finish()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_LIST) {

                cameraUri = data!!.data!!
                coverInitialized = true
                val uri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                    applicationContext.contentResolver,
                    uri
                )
                val input = contentResolver?.openInputStream(uri!!)
                val image = BitmapFactory.decodeStream(input, null, null)

            } else if (requestCode == PICK_IMAGE_BACKGROUND) {
                cameraUri = data!!.data!!
                coverInitialized = true
                val uri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                    applicationContext.contentResolver,
                    uri
                )
                binding.imageView14.setImageBitmap(bitmap)


            } else if (requestCode == PICK_DOODLE_LIST) {
                cameraUri = data!!.data!!
                coverInitialized = true
                val uri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                    applicationContext.contentResolver,
                    uri
                )


            } else {
                when (requestCode) {
                    1 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image1)
                        list.add(cameraUri)

                    }
                    2 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image2)
                        list.add(cameraUri)
                    }
                    3 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image3)
                        list.add(cameraUri)
                    }
                    4 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image4)
                        list.add(cameraUri)
                    }
                    5 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image5)
                        list.add(cameraUri)
                    }
                    6 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image6)
                        list.add(cameraUri)
                    }
                    7 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image7)
                        list.add(cameraUri)
                    }
                    8 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image8)
                        list.add(cameraUri)
                    }
                    9 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image9)
                        list.add(cameraUri)
                    }
                    10 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image10)
                        list.add(cameraUri)
                    }
                    11 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image11)
                        list.add(cameraUri)

                    }
                    12 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image12)
                        list.add(cameraUri)
                    }
                    13 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image13)
                        list.add(cameraUri)
                    }
                    14 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image14)
                        list.add(cameraUri)
                    }
                    15 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image15)
                        list.add(cameraUri)
                    }
                    16 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image16)
                        list.add(cameraUri)
                    }
                    17 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image17)
                        list.add(cameraUri)
                    }
                    18 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image18)
                        list.add(cameraUri)
                    }
                    19 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image19)
                        list.add(cameraUri)
                    }
                    20 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image20)
                        list.add(cameraUri)
                    }
                    21 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image21)
                        list.add(cameraUri)

                    }
                    22 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image22)
                        list.add(cameraUri)
                    }
                    23 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image23)
                        list.add(cameraUri)
                    }
                    24 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image24)
                        list.add(cameraUri)
                    }
                    25 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image25)
                        list.add(cameraUri)
                    }
                    26 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image26)
                        list.add(cameraUri)
                    }
                    27 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image27)
                        list.add(cameraUri)
                    }
                    28 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image28)
                        list.add(cameraUri)
                    }
                    29 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image29)
                        list.add(cameraUri)
                    }
                    30 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image30)
                        list.add(cameraUri)
                    }
                }
            }
        }

    }

    override fun onclick(_position: Int) {
        super.onclick(_position)

        this.position = _position

        val intent2 = Intent()
        intent2.type = "image/*"
        intent2.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent2, "Select Picture"), PICK_DOODLE_LIST
        )

        val cameraGalleryDialog =
            CameraGalleryDialog(this, object : OnDialogClickListener {

                override fun onDialogClick(callBack: String?) {
                    if (callBack == "Camera") {
                        if (!Permissions.Check_CAMERA(this@PackActivity) || !Permissions.Check_STORAGE(
                                this@PackActivity
                            )
                        ) {
                            Permissions.Request_CAMERA_STORAGE(this@PackActivity, 11)
                        } else {
//

                        }
                    } else if (callBack == "Gallery") {
                        if (!Permissions.Check_STORAGE(this@PackActivity)) {
                            Permissions.Request_STORAGE(this@PackActivity, 22)
                        } else {
                            val intent2 = Intent()
                            intent2.type = "image/*"
                            intent2.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(
                                Intent.createChooser(intent2, "Select Picture"), PICK_DOODLE_LIST
                            )
                        }
                    }
                }
            })
        cameraGalleryDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        cameraGalleryDialog.show()

    }


}