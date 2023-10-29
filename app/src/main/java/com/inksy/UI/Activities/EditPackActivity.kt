package com.inksy.UI.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.example.DoodlePack
import com.example.example.Doodles
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.AddDoodleAdapter
import com.inksy.UI.Constants
import com.inksy.UI.ViewModel.DoodleView
import com.inksy.Utils.FileUtil
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityEditpackBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.File

class EditPackActivity : AppCompatActivity(), iOnClickListerner {
    private val PICK_IMAGE_BACKGROUND = 200
    private val CAMERA_REQUEST = 52
    private val PICK_IMAGE_LIST = 53
    private val PICK_DOODLE_LIST = 10
    lateinit var doodleView: DoodleView
    lateinit var binding: ActivityEditpackBinding
    lateinit var tinydb: TinyDB
    var token = ""
    lateinit var cameraUri: Uri
    var coverInitialized = false
    var list: ArrayList<Uri> = ArrayList()
    var position = 0
    var wholedata: DoodlePack? = DoodlePack()
    var doodleList: ArrayList<Doodles> = ArrayList()
    lateinit var adapter: AddDoodleAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditpackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var packId = intent.getStringExtra("PackId")

        cameraUri = Uri.parse("0")
        tinydb = TinyDB(this)
        token = tinydb.getString("token").toString()
        doodleView = ViewModelProvider(this)[DoodleView::class.java]
        doodleView.init()
        var pricerange = tinydb.getString("pricerange")
        var data = pricerange?.split("-")
        var pricemin = "0.9"
        var pricemax = "4.9"

        binding.slider.valueFrom = pricemin?.toFloat()!!
        binding.slider.valueTo = pricemax?.toFloat()!!
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
            binding.title.requestFocus()
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
            this.finish()
        }
        binding.checked.setOnClickListener {
            var file: File = FileUtil.from(this, cameraUri)
            var price = binding.slider.value.toString() + "9"


            upload(token, binding.title.text.toString(), price, file)


        }
        binding.buyNow.setOnClickListener {

            this.finish()
        }
        binding.slider.addOnChangeListener { slider, value, fromUser ->
            val number2digits: Double = String.format("%.2f", value).toDouble()
            binding.price2.text = "Your Price is : $ ${number2digits}"
        }

        binding.imageViewTitle.setOnClickListener {
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
        cancel()
        getData(packId!!, token)
    }

    fun upload(token: String, packTitle: String, packPrice: String, file: File) {

        doodleView.editDoodle(packTitle, packPrice, wholedata?.id!!, file, token)?.observe(this) {
            when (it?.status) {
                Status.LOADING -> {}
                Status.ERROR -> {}
                Status.SUCCESS -> {
                    var _it = it?.data?.data
                    Toast.makeText(this, it?.data?.message, Toast.LENGTH_SHORT).show()

                    this.finish()
                } else -> {}
            }
        }
    }


    private fun addDoodle(id: Int, uri: Uri) {


        var file: File = FileUtil.from(this, uri)

        val doodlePackId: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), id.toString())
        var cover_image: RequestBody = RequestBody.create(".png".toMediaTypeOrNull(), file)

        doodleView.addDoodle(doodlePackId, cover_image, token)?.observe(this) {
            when (it?.status) {
                Status.LOADING -> {}
                Status.ERROR -> {}
                Status.SUCCESS -> {
                    Toast.makeText(this, it?.data?.message, Toast.LENGTH_SHORT).show()
                } else -> {}
            }
        }


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
                binding.imageViewTitle.setImageBitmap(bitmap)


            } else if (requestCode == PICK_DOODLE_LIST) {
                var doodlecameraUri = data!!.data!!
                coverInitialized = true
                val uri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                    applicationContext.contentResolver,
                    uri
                )


            } else {
                var doodlecameraUri = data!!.data!!
                when (requestCode) {
                    1 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image1)
                        addDoodle(wholedata?.id!!, doodlecameraUri)

                    }
                    2 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image2)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    3 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image3)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    4 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image4)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    5 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image5)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    6 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image6)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    7 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image7)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    8 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image8)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    9 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image9)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    10 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image10)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    11 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image11)
                        addDoodle(wholedata?.id!!, doodlecameraUri)

                    }
                    12 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image12)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    13 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image13)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    14 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image14)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    15 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image15)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    16 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image16)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    17 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image17)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    18 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image18)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    19 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image19)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    20 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image20)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    21 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image21)
                        addDoodle(wholedata?.id!!, doodlecameraUri)

                    }
                    22 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image22)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    23 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image23)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    24 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image24)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    25 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image25)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    26 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image26)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    27 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image27)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    28 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image28)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    29 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image29)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
                    }
                    30 -> {
                        cameraUri = data!!.data!!
                        Glide.with(this).load(cameraUri).placeholder(R.drawable.add_doodle)
                            .into(binding.image30)
                        addDoodle(wholedata?.id!!, doodlecameraUri)
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

    }

    fun getData(doodlePackId: String, token: String) {
        doodleView.doodleDetails(doodlePackId, token)?.observe(this) {
            binding.loader.visibility = View.GONE
            when (it.status) {
                Status.SUCCESS -> {

                    wholedata = it?.data?.data
                    binding.title.setText(wholedata?.packTitle)
                    when {
                        wholedata?.price?.toFloat()!! < 1.0 -> {
                            binding.slider.value = 0.9f
                        }
                        wholedata?.price?.toFloat()!! < 2.0 -> {
                            binding.slider.value = 1.9f
                        }
                        wholedata?.price?.toFloat()!! < 3.0 -> {
                            binding.slider.value = 2.9f
                        }
                        wholedata?.price?.toFloat()!! < 4.0 -> {
                            binding.slider.value = 3.9f
                        }
                        wholedata?.price?.toFloat()!! < 5.0 -> {
                            binding.slider.value = 4.9f
                        }
                    }
//                    when {
//
//                    }
//                    when {
//
//                    }
//                    when {
//
//                        else -> {
//                            binding.slider.value = 2.9f
//                        }
//                    }


                    Glide.with(this).load(Constants.BASE_IMAGE + wholedata?.coverImage)
                        .into(binding.imageViewTitle)


                    doodleList = it?.data?.data?.doodles!!
                    for (i in 0 until doodleList?.size!!) {
                        when (i) {
                            0 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image1)
                                binding.cross1.visibility= View.VISIBLE
                            }
                            1 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image2)
                                binding.cross2.visibility= View.VISIBLE
                            }
                            2 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image3)
                                binding.cross3.visibility= View.VISIBLE
                            }
                            3 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image4)
                                binding.cross4.visibility= View.VISIBLE
                            }
                            4 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image5)
                                binding.cross5.visibility= View.VISIBLE
                            }
                            5 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image6)
                                binding.cross6.visibility= View.VISIBLE
                            }
                            6 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image7)
                                binding.cross7.visibility= View.VISIBLE
                            }
                            7 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image8)
                                binding.cross8.visibility= View.VISIBLE
                            }
                            8 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image9)
                                binding.cross9.visibility= View.VISIBLE
                            }
                            9 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image10)
                                binding.cross10.visibility= View.VISIBLE
                            }
                            10 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image11)
                                binding.cross11.visibility= View.VISIBLE
                            }
                            11 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image12)
                                binding.cross12.visibility= View.VISIBLE
                            }
                            12 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image13)
                                binding.cross13.visibility= View.VISIBLE
                            }
                            13 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image14)
                                binding.cross14.visibility= View.VISIBLE
                            }
                            14 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image15)
                                binding.cross15.visibility= View.VISIBLE
                            }
                            15 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image16)
                                binding.cross16.visibility= View.VISIBLE
                            }
                            16 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image17)
                                binding.cross17.visibility= View.VISIBLE
                            }
                            17 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image18)
                                binding.cross18.visibility= View.VISIBLE
                            }
                            18 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image19)
                                binding.cross19.visibility= View.VISIBLE
                            }
                            19 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image20)
                                binding.cross20.visibility= View.VISIBLE
                            }
                            20 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image21)
                                binding.cross21.visibility= View.VISIBLE
                            }
                            21 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image22)
                                binding.cross22.visibility= View.VISIBLE
                            }
                            22 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image23)
                                binding.cross23.visibility= View.VISIBLE
                            }
                            23 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image24)
                                binding.cross24.visibility= View.VISIBLE
                            }
                            24 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image25)
                                binding.cross25.visibility= View.VISIBLE
                            }
                            25 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image26)
                                binding.cross26.visibility= View.VISIBLE
                            }
                            26 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image27)
                                binding.cross27.visibility= View.VISIBLE
                            }
                            27 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image28)
                                binding.cross28.visibility= View.VISIBLE
                            }
                            28 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image29)
                                binding.cross29.visibility= View.VISIBLE
                            }
                            29 -> {
                                Glide.with(this)
                                    .load(Constants.BASE_IMAGE + wholedata?.doodles?.get(i)?.doodleImage)
                                    .into(binding.image30)
                                binding.cross30.visibility= View.VISIBLE
                            }

                        }
                    }

                }
                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }
    }

    fun cancel() {
        binding.cross1.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross2.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross3.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross4.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross5.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross6.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross7.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross8.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross9.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross10.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross11.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross12.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross13.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross14.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross15.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross16.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross17.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross18.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross19.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross20.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross21.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross22.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross23.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross24.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross25.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross26.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross27.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross28.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross29.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
        binding.cross30.setOnClickListener {
            deleteDoodle(doodleList[0].id!!, doodleList[0].doodlePackId!!, token)
        }
    }

    fun deleteDoodle(doodleId: Int, doodlePackId: Int, token: String) {
        doodleView.deleteDoodle(doodleId, doodlePackId, token)?.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(this, it?.data?.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(this, it?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}