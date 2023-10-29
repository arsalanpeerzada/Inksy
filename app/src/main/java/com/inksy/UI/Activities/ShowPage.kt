package com.inksy.UI.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.burhanrashid52.photoediting.*
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.inksy.Database.Entities.JournalIndexTable
import com.inksy.Database.JournalDatabase
import com.inksy.Interfaces.OnDialogBulletClickListener
import com.inksy.Interfaces.PopUpOnClickListerner
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Interfaces.onMoveListener
import com.inksy.Model.Styles
import com.inksy.Model.TransformInfo
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Adapter.BulletAdapter
import com.inksy.UI.Adapter.BulletSelectAdapter
import com.inksy.UI.Adapter.PagesAdapter
import com.inksy.UI.Constants
import com.inksy.UI.Dialogs.BulletDialog
import com.inksy.UI.ViewModel.JournalView
import com.inksy.Utils.FileUtil
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityShowPageBinding
import io.github.hyuwah.draggableviewlib.DraggableView
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.TextStyleBuilder
import ja.burhanrashid52.photoeditor.ViewType
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder
import ja.burhanrashid52.photoeditor.shape.ShapeType
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

class ShowPage : AppCompatActivity(), iOnClickListerner, OnPhotoEditorListener,
    PropertiesBSFragment.Properties, ShapeBSFragment.Properties, EmojiBSFragment.EmojiListener,
    StickerBSFragment.StickerListener, OnDialogBulletClickListener, PopUpOnClickListerner,
    View.OnFocusChangeListener, View.OnClickListener, onMoveListener
{
    private lateinit var bottomSheetDialog: RoundedBottomSheetDialog
    lateinit var tinyDB: TinyDB
    var listofTransform = ArrayList<TransformInfo>()
    lateinit var transinfo: TransformInfo
    var imageArray: JSONArray = JSONArray()
    var textArray: JSONArray = JSONArray()
    var bulletArray: JSONArray = JSONArray()
    var brushArray: JSONArray = JSONArray()
    lateinit var llTestDraggable: DraggableView<LinearLayout>
    private var layoutDragActive = " "
    private var layoutcount: Int = 0
    var fragmentName = Constants.CREATEJOURNALINDEX
    private var mShapeBSFragment: ShapeBSFragment? = null
    private var mShapeBuilder: ShapeBuilder? = null
    private val PICK_IMAGE_BACKGROUND = 2

    private lateinit var cover_cameraUri: Uri
    lateinit var jouralView: JournalView
    lateinit var mPhotoEditor: PhotoEditor
    private val CAMERA_REQUEST = 52
    private val PICK_REQUEST = 53
    val PINCH_TEXT_SCALABLE_INTENT_KEY = "PINCH_TEXT_SCALABLE"
    var coverTitle = ""
    var categoryId = 0
    var coverDesciption = ""
    var cover_color = ""
    var categoryname = ""
    lateinit var cameraUri: Uri
    lateinit var styles: Styles
    lateinit var bulletList: ArrayList<Styles>
    private var selectedFile: File? = null
    var pageId: Int = 0
    lateinit var journalDatabase: JournalDatabase
    var backgroundImage = ""
    var token = ""
    lateinit var data: JournalIndexTable
    lateinit var binding: ActivityShowPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tinyDB = TinyDB(this)
        token = tinyDB.getString("token").toString()
        transinfo = TransformInfo(0f, 0f, 0f, 0f, 0f, 0f, 0f, 10f)


        pageId = intent.getIntExtra("Page", 0)

        journalDatabase = JournalDatabase.getInstance(this)!!

        binding.textView20.text = pageId.toString()

        bulletArray = JSONArray()

        init()
        initClickListener()

        jouralView = ViewModelProvider(this)[JournalView::class.java]
        jouralView.init()


        var data = journalDatabase.getJournalData().getOnePage(pageId.toString())


        binding.nextpage.setOnClickListener {
            var pages = journalDatabase.getJournalData().getAllPages()

            var pagerows = pages.size


            openRoundBottomSheet(pagerows)
        }

        binding.photoEditorView.background =
            ContextCompat.getDrawable(this, R.drawable.whiteimage)

        if (data.pageBackground.isNullOrEmpty()) {

        } else {
            Glide.with(this)
                .asBitmap()
                .load(Constants.BASE_IMAGE + data.pageBackground)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {

                        val d: Drawable = BitmapDrawable(resource)

                        binding.photoEditorView.background = d
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }
                })

        }

        if (pageId != null) {
            var indexdata = journalDatabase.getJournalData().getOnePage(pageId.toString())

            val gson = Gson()
            if (indexdata != null) {

                if (indexdata.pageBackground.isNullOrEmpty()) {

                } else {
                    Glide.with(this)
                        .asBitmap()
                        .load(Constants.BASE_IMAGE + indexdata.pageBackground)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                val d: Drawable = BitmapDrawable(resources, resource)
                                binding.photoEditorView.setBackgroundDrawable(d)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                // this is called when imageView is cleared on lifecycle call or for
                                // some other reason.
                                // if you are referencing the bitmap somewhere else too other than this imageView
                                // clear it here as you can no longer have the bitmap
                            }
                        })
                }

                if (indexdata.arrayOfBullets != null && indexdata.arrayOfBullets != "[]") {
                    bulletArray = JSONArray(indexdata.arrayOfBullets)
                    val index = bulletArray.length()
                    for (i in 0 until index) {
                        val _jsonObject = bulletArray.getJSONObject(i)
                        val title = _jsonObject.getString("title")
                        val bulletList = _jsonObject.get("bullet") as JSONArray
                        val type = _jsonObject.getInt("type")
                        val axixX = _jsonObject.getString("axixX")
                        val axixY = _jsonObject.getString("axixY")
                        val layoutName = _jsonObject.getString("layoutID")
                        val titleModel: Styles = gson.fromJson(title, Styles::class.java)
                        //var bullets = gson.fromJson(bulletList, Styles::class.java)

                        val _index = bulletList.length()

                        val bullets: ArrayList<Styles> = ArrayList()
                        for (j in 0 until _index) {
                            val item = bulletList.getString(j)
                            val bullet: Styles = gson.fromJson(item, Styles::class.java)
                            bullets.add(bullet)
                        }
                        ondialogClick(
                            titleModel,
                            bullets,
                            type = type,
                            layoutName,
                            axixX.toFloat(),
                            axixY.toFloat(),
                        )
                    }
                }
                if (indexdata.arrayOfText != null && indexdata.arrayOfText != "[]") {
                    textArray = JSONArray(indexdata.arrayOfText)
                    val textarray = textArray.length()
                    for (k in 0 until textarray) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            setTextArray(k)
                        }
                    }
                }

                if (indexdata.arrayOfImage != null && indexdata.arrayOfImage != "[]") {
                    imageArray = JSONArray(indexdata.arrayOfImage)
                    val imagearray = imageArray.length()
                    for (l in 0 until imagearray) {

                        val imageJsonObject = imageArray.getJSONObject(l)
                        val width: String = imageJsonObject.getString("width")
                        val height: String = imageJsonObject.getString("height")
                        val bit = imageJsonObject.getString("bitmap")
                        val axixX = imageJsonObject.getString("axixX")
                        val axixY = imageJsonObject.getString("axixY")
                        val info = imageJsonObject.getString("info")
                        val viewtag = imageJsonObject.getString("viewtag")

//                        val charset = Charsets.UTF_8
//
//                        val imageBytes = Base64.decode(bit, Base64.DEFAULT)
//                        val decodedImage =
//                            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                        var decodedImage: Bitmap


                        Glide.with(this)
                            .asBitmap()
                            .load(Constants.BASE_IMAGE + bit)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    mPhotoEditor.addImage(
                                        resource,
                                        axixX.toFloat(),
                                        axixY.toFloat(),
                                        width.toFloat(),
                                        height.toFloat(),
                                        viewtag, object : onMoveListener {
                                            override fun onMove(view: View, info: TransformInfo) {
                                                super.onMove(view, info)
                                                transinfo = info
                                            }
                                        },
                                        false
                                    )
                                    if (info.isNullOrEmpty()) {

                                    } else {
                                        val _info: TransformInfo =
                                            gson.fromJson(info, TransformInfo::class.java)
                                        for (l in 3 until binding.photoEditorView.childCount) {
                                            var view =
                                                binding.photoEditorView.getChildAt(l) as FrameLayout

                                            var mtag = view.tag
                                            if (mtag == _info.viewTag) {
                                              //  ShowJournal.moveFinal(view, _info)
                                            }

                                        }
                                    }
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                    // this is called when imageView is cleared on lifecycle call or for
                                    // some other reason.
                                    // if you are referencing the bitmap somewhere else too other than this imageView
                                    // clear it here as you can no longer have the bitmap
                                }
                            })
                    }
                }
            }
            styles = Styles()
            bulletList = ArrayList()
            mShapeBSFragment = ShapeBSFragment()
            mShapeBSFragment!!.setPropertiesChangeListener(this)

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTextArray(k: Int) {
        val jsonObject = textArray.getJSONObject(k)
        val inputText: String = jsonObject.get("text") as String
        val colorCode: Int = jsonObject.get("colorcode") as Int
        val _typface: Int = jsonObject.get("typeface") as Int
        val isBold: Boolean = jsonObject.get("bold") as Boolean
        val isItalic: Boolean = jsonObject.get("italic") as Boolean
        val axixX: String = jsonObject.getString("axixX")
        val axixY: String = jsonObject.getString("axixY")
        val width: String = jsonObject.getString("width")
        val height: String = jsonObject.getString("height")
        val viewtag: String = jsonObject.getString("viewtag")
        var info: String = jsonObject.getString("info")


        val styleBuilder = TextStyleBuilder()
        styleBuilder.withTextColor(colorCode)
        styleBuilder.withTextSize(30f)

        if (_typface != 0) {
            val typeface = resources?.getFont(_typface)
            styleBuilder.withTextFont(typeface!!)
        }

        if (isBold && isItalic) {
            styleBuilder.withTextStyle(Typeface.BOLD_ITALIC)

        } else if (isBold) {
            styleBuilder.withTextStyle(Typeface.BOLD)

        } else if (isItalic) {
            styleBuilder.withTextStyle(Typeface.ITALIC)
        }

        textArray.put(jsonObject)
        mPhotoEditor.addText(
            inputText,
            styleBuilder,
            axixX.toFloat(),
            axixY.toFloat(),
            width.toFloat(),
            height.toFloat(),
            viewtag,
            this,
            false,
        )

        if (info != "") {
            var gson = Gson()
            val _info: TransformInfo =
                gson.fromJson(info, TransformInfo::class.java)
            for (l in 3 until binding.photoEditorView.childCount) {
                var view =
                    binding.photoEditorView.getChildAt(l) as FrameLayout

                var mtag = view.tag
                if (mtag == _info.viewTag) {
                    moveFinal(view, _info)
                }

            }
        }
    }


    private fun showBottomSheetDialogFragment(fragment: BottomSheetDialogFragment?) {
        if (fragment == null || fragment.isAdded) {
            return
        }
        fragment.show(supportFragmentManager, fragment.tag)
    }

    fun init() {
        val pinchTextScalable =
            this.intent.getBooleanExtra(PINCH_TEXT_SCALABLE_INTENT_KEY, false)


        //Typeface mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");
        mPhotoEditor = PhotoEditor.Builder(this, binding.photoEditorView)
            .setPinchTextScalable(pinchTextScalable) // set flag to make text scalable when pinch
            //.setDefaultTextTypeface(mTextRobotoTf)
            //.setDefaultEmojiTypeface(mEmojiTypeFace)
            .build() // build photo editor sdk
       // mPhotoEditor.setOnPhotoEditorListener(this)

        //Set Image Dynamically

        binding.photoEditorView.source!!.setImageResource(R.drawable.transparentdraw)
        binding.photoEditorView.background =
            ContextCompat.getDrawable(this, R.drawable.paris_tower)
    }

    private fun initClickListener() {
        binding.bullets.setOnClickListener {
            val array = arrayOf(
                R.drawable.bullets_list, R.drawable.checkbox,
                R.drawable.numbers_list, R.drawable.alphabetic
            )

            binding.itemList.adapter = BulletSelectAdapter(this, array, this)

            if (binding.itemList.visibility == View.VISIBLE) {
                binding.itemList.visibility = View.GONE
            } else {
                binding.itemList.visibility = View.VISIBLE
            }
        }
        binding.ivBack.setOnClickListener {
            this.finish()
        }
        binding.checked.visibility = View.GONE
        binding.checked.setOnClickListener {

            this.finish()

        }

        binding.picture.setOnClickListener {
            //Add drawable sticker

            binding.picture.setOnClickListener {
                //Add drawable sticker


                val contextWrapper = ContextThemeWrapper(this, R.style.popupMenuStyle)
                val popupMenu = PopupMenu(
                    contextWrapper, binding.picture
                )
                popupMenu.setForceShowIcon(true)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->

                    when (item.itemId) {
                        R.id.Delete -> {

                            return@OnMenuItemClickListener true
                        }
                        R.id.edit -> {

                            return@OnMenuItemClickListener true
                        }
                        R.id.Report -> {


                            return@OnMenuItemClickListener true
                        }
                        R.id.block -> {

                            return@OnMenuItemClickListener true
                        }
                        R.id.gallery -> {

                            val intent2 = Intent()
                            intent2.type = "image/*"
                            intent2.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(
                                Intent.createChooser(intent2, "Select Picture"), PICK_REQUEST
                            )

                            return@OnMenuItemClickListener true
                        }
                        R.id.doodle -> {
                            return@OnMenuItemClickListener true
                        }

                        else -> false
                    }


                })
                popupMenu.inflate(R.menu.view_journal_popup)
                popupMenu.show()

                popupMenu.menu.findItem(R.id.edit).isVisible = false
                popupMenu.menu.findItem(R.id.Delete).isVisible = false
                popupMenu.menu.findItem(R.id.View).isVisible = false
                popupMenu.menu.findItem(R.id.Report).isVisible = false
                popupMenu.menu.findItem(R.id.block).isVisible = false
                popupMenu.menu.findItem(R.id.Home).isVisible = false


            }
        }

        binding.backgroundPicture.setOnClickListener {
            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), PICK_IMAGE_BACKGROUND
            )
        }

        binding.doodle.setOnClickListener {

            mPhotoEditor.setBrushDrawingMode(true)
            mShapeBuilder = ShapeBuilder()
            mPhotoEditor.setShape(mShapeBuilder)
            showBottomSheetDialogFragment(mShapeBSFragment)

        }

        binding.text.setOnClickListener {

            onTextCreateMethod()
        }

        binding.buttonUndo.setOnClickListener {
            mPhotoEditor.undo()
        }

        binding.buttonRedo.setOnClickListener {
            mPhotoEditor.redo()
        }

        binding.plus.setOnClickListener {


        }
    }

    override fun onBackPressed() {
        this.finish()
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {

        Log.d("ViewCheck", v.toString())
    }

    override fun onClick(v: View?) {

        // This code is responsible for selecting/drag and drop indivisual bullet layouts

//        if (layoutDragActive == v?.tag) {
//            val layout = v as LinearLayout
//
//            llTestDraggable.disableDrag()
//            layoutDragActive = ""
//            var data = layout.background
//            layout.setBackgroundResource(R.color.transparent)
//
//        } else {
//            val layout = v as LinearLayout
//            layout.setBackgroundResource(R.drawable.border_layout_grey)
//
//            layoutDragActive = v.tag.toString()
//
//
//            llTestDraggable = DraggableView.Builder(layout)
//                .setStickyMode(DraggableView.Mode.NON_STICKY)
//                .build()
//        }
//
//
//        for (i in 0 until binding.texteditor.size) {
//
//            val layout = binding.texteditor.getChildAt(i)
//
//            if (layoutDragActive != layout.tag.toString()) {
//                layout.setBackgroundResource(R.color.transparent)
//
//
//            }
//        }
    }

    private fun openPopUp(data: String, itemView: View, layoutType: String) {
        val contextWrapper = ContextThemeWrapper(this, R.style.popupMenuStyle)
        val popupMenu = PopupMenu(
            contextWrapper, itemView
        )
        popupMenu.setForceShowIcon(true)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
//                R.id.link -> {
//
//                    return@OnMenuItemClickListener true
//                }
                R.id.Edit -> {
                    var id: String = ""
                    if (layoutType == "item") {
                        val recyclerView = itemView.parent as RecyclerView
                        val layout = recyclerView.parent as LinearLayout
                        id = layout.tag.toString()

                        val textview = layout.getChildAt(1) as TextView

                        styles = Styles(textview.text.toString())


                    } else {
                        val textview = itemView as TextView
                        val layout = textview.parent as LinearLayout
                        id = layout.tag.toString()
                    }

                    openDialog(data.toInt(), true, id)

                    return@OnMenuItemClickListener true
                }
                else -> false
            }
        })
        popupMenu.inflate(R.menu.pop_up)
        popupMenu.show()
        popupMenu.menu.findItem(R.id.link_bullet).isVisible = false
        popupMenu.menu.findItem(R.id.link_title).isVisible = false
        if (data == "Edit" || data == "title") {
            popupMenu.menu.findItem(R.id.Edit).isVisible = false
        }

    }

    override fun onclick(position: Int) {
        super.onclick(position)

        openDialog(position, false, "")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_REQUEST) {
                cameraUri = data!!.data!!

                val uri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                    this.applicationContext.contentResolver,
                    uri
                )


                var file: File = FileUtil.from(this, cameraUri)

                jouralView.imageUpload(token, file)?.observe(this) {
                    when (it?.status) {
                        Status.SUCCESS -> {
                            val ran = (1..1000).random()
                            var viewtag = "IMAGE_$ran"
                            mPhotoEditor.addImage(bitmap, 0f, 0f, 0f, 0f, viewtag, this, false)
                            val json = JSONObject()
                            json.put("bitmap", it?.data?.data?.avatar.toString())
                            json.put("axixX", "0")
                            json.put("axixY", "0")
                            json.put("viewtag", viewtag)
                            imageArray.put(json)
                        }
                        Status.ERROR -> {}
                        Status.LOADING -> {}
                    }
                }

//                val input = activity?.contentResolver?.openInputStream(cameraUri)
//                val image = BitmapFactory.decodeStream(input, null, null)
//
//                // Encode image to base64 string
//                val baos = ByteArrayOutputStream()
//                image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//                val imageBytes = baos.toByteArray()
//                val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)


            } else if (requestCode == PICK_IMAGE_BACKGROUND) {


                cameraUri = data!!.data!!

                val uri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                    this.applicationContext.contentResolver,
                    uri
                )
                val d: Drawable = BitmapDrawable(resources, bitmap)
                var file: File = FileUtil.from(this, cameraUri)
                jouralView.imageUpload(token, file)?.observe(this) {
                    when (it?.status) {
                        Status.SUCCESS -> {
                            binding.photoEditorView.setBackgroundDrawable(d)
                            backgroundImage = it?.data?.data?.avatar.toString()
                        }
                        Status.ERROR -> {}
                        Status.LOADING -> {}
                    }
                }


            } else if (requestCode == CAMERA_REQUEST) {
                val selectedFilePath: String = FileUtil.getPath(this, cameraUri)
                val file = File(selectedFilePath)
                val compressedImageFile: File? = null
                try {
                    cameraUri = Uri.fromFile(compressedImageFile)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                selectedFile = File(cameraUri.path!!)

            }
        }

    }

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        //   Toast.makeText(this, "View Added", Toast.LENGTH_SHORT).show()
    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int, tag: String) {
       // TODO("Not yet implemented")
    }


    override fun onStartViewChangeListener(viewType: ViewType?) {
        //  Toast.makeText(this, "View Change", Toast.LENGTH_SHORT).show()
    }

    override fun onStopViewChangeListener(viewType: ViewType?) {
        //   Toast.makeText(this, "View Change Stop", Toast.LENGTH_SHORT).show()
    }

    override fun onTouchSourceImage(event: MotionEvent?) {

    }

    override fun onEmojiClick(emojiUnicode: String?) {

    }

    override fun onColorChanged(colorCode: Int) {

        if (colorCode == 1) {
            mPhotoEditor.brushEraser()
        } else {
            mPhotoEditor.setBrushDrawingMode(true)
            mPhotoEditor.setShape(mShapeBuilder!!.withShapeColor(colorCode))
        }

    }

    override fun onOpacityChanged(opacity: Int) {
        mPhotoEditor.setShape(mShapeBuilder!!.withShapeOpacity(opacity))
    }


    override fun onShapeSizeChanged(shapeSize: Int) {
        mPhotoEditor.setShape(mShapeBuilder!!.withShapeSize(shapeSize.toFloat()))
    }

    override fun onShapePicked(shapeType: ShapeType?) {
        mPhotoEditor.setShape(mShapeBuilder!!.withShapeType(shapeType))
    }


    override fun onStickerClick(bitmap: Bitmap?) {

    }


    private fun checkTextStyle(styles: Styles): String {
        var data = styles.data

        if (styles.isBold!!) {
            // holder.et.setTextAppearance(R.style.boldText)
            data = "<b>$data</b>"
        }
        if (styles.isItalic!!) {
            // holder.et.setTextAppearance(R.style.italicText)
            data = "<i>$data</i>"
        }
        if (styles.isunderline!!) {
            data = "<u>$data</u>"
        }
        if (styles.isstrike!!) {
            data = "<s>$data</s>"
        }

        return data!!
    }

    private fun onTextCreateMethod() {
        val textEditorDialogFragment =
            TextEditorDialogFragment.show(this as AppCompatActivity)
        textEditorDialogFragment.setOnTextEditorListener(object :
            TextEditorDialogFragment.TextEditor {


            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDone(
                inputText: String?,
                colorCode: Int,
                _typface: Int,
                isBold: Boolean,
                isItalic: Boolean
            ) {

                val jsonObject = JSONObject()
                jsonObject.put("text", inputText)
                jsonObject.put("colorcode", colorCode)
                jsonObject.put("typeface", _typface)

                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                styleBuilder.withTextSize(30f)


                if (_typface != 0) {
                    val typeface = resources?.getFont(_typface)
                    styleBuilder.withTextFont(typeface!!)
                }

                if (isBold && isItalic) {
                    styleBuilder.withTextStyle(Typeface.BOLD_ITALIC)
                    jsonObject.put("bold", true)
                    jsonObject.put("italic", true)
                } else if (isBold) {
                    styleBuilder.withTextStyle(Typeface.BOLD)
                    jsonObject.put("bold", true)
                    jsonObject.put("italic", false)
                } else if (isItalic) {
                    styleBuilder.withTextStyle(Typeface.ITALIC)
                    jsonObject.put("bold", false)
                    jsonObject.put("italic", true)
                } else {
                    jsonObject.put("bold", false)
                    jsonObject.put("italic", false)
                }
                val ran = (1..1000).random()
                var viewtag = "TEXT_$ran"

                jsonObject.put("axixX", 0f)
                jsonObject.put("axixY", 0f)
                jsonObject.put("viewtag", viewtag)

                textArray.put(jsonObject)

                mPhotoEditor.addText(
                    inputText,
                    styleBuilder,
                    0f,
                    0f,
                    0f,
                    0f,
                    viewtag,
                    object : onMoveListener {
                        override fun onMove(view: View, info: TransformInfo) {
//                            super.onMove(view, info)
//                            transinfo = info

//                            if (listofTransform.size == 0) {
//                                listofTransform.add(transinfo)
//                            } else {
//                                var contain = listofTransform.any {
//                                    it.viewTag == transinfo.viewTag
//                                }
//                                if (contain) {
//                                    for (i in 0 until listofTransform.size) {
//
//                                        if (listofTransform[i].viewTag == transinfo.viewTag) {
//                                            listofTransform.removeAt(i)
//                                            listofTransform.add(i, transinfo)
//                                            break
//
//                                        }
//                                    }
//                                } else {
//                                    listofTransform.add(transinfo)
//                                }
//                            }
                        }
                    }, false
                )
            }
        })
    }


    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
        val textEditorDialogFragment =
            TextEditorDialogFragment.show(this as AppCompatActivity, text!!, colorCode)
        textEditorDialogFragment.setOnTextEditorListener(object :
            TextEditorDialogFragment.TextEditor {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDone(
                inputText: String?,
                colorCode: Int,
                _typface: Int,
                isBold: Boolean,
                isItalic: Boolean
            ) {

                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                styleBuilder.withTextSize(30f)

                if (_typface != 0) {
                    val typeface = resources?.getFont(_typface)
                    styleBuilder.withTextFont(typeface!!)
                }
                if (isBold && isItalic) {
                    styleBuilder.withTextStyle(Typeface.BOLD_ITALIC)
                } else if (isBold) {
                    styleBuilder.withTextStyle(Typeface.BOLD)
                } else if (isItalic) {
                    styleBuilder.withTextStyle(Typeface.ITALIC)
                }
                mPhotoEditor.editText(rootView!!, inputText, styleBuilder)
            }
        })
    }

    override fun onDialogClick(
        callBacktv: Styles,
        callbackrv: ArrayList<Styles>,
        type: Int,
        layoutid: String
    ) {
        //  var styles = callBacktv

        ondialogClick(callBacktv, callbackrv, type, layoutid, 0f, 0f)
    }

    private fun ondialogClick(
        callBacktv: Styles,
        callbackrv: ArrayList<Styles>,
        type: Int,
        layoutid: String,
        _axixX: Float,
        _axixY: Float
    ) {

        val linearLayout = LinearLayout(this)
        linearLayout.setPadding(40)
        var linearlayoutparams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.layoutParams = linearlayoutparams
        linearLayout.orientation = LinearLayout.VERTICAL

        var x = _axixX
        var y = _axixY
        linearLayout.x = x
        linearLayout.y = y

        var rv = callbackrv
        if (callBacktv.data != "") {

            binding.texteditor.requestFocus()
            val view = View(this)
            val textview = TextView(this)
            textview.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            textview.tag = "textview_$layoutcount"
            val finaldata = checkTextStyle(callBacktv)
            textview.text = Html.fromHtml(finaldata)
            textview.setTextColor(callBacktv.textColor!!)
            textview.textSize = callBacktv.fontsize?.toFloat()!!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textview.typeface = this.resources.getFont(callBacktv.typeface!!)
            }

            view.layoutParams = LinearLayout.LayoutParams(
                20,
                20
            )
            if (callBacktv.textColor == 0) {
                textview.setTextColor(this.resources.getColor(R.color.black))
            }

            linearLayout.addView(view)
            linearLayout.addView(textview)


//            textview.setOnLongClickListener {
//                //title edit
//                openPopUp("title", textview, "title")
//                true
//            }


        }
        if (callbackrv.size > 0) {

            bulletList = callbackrv
            val recycler = RecyclerView(this)
            recycler.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            recycler.layoutManager = LinearLayoutManager(this)
            recycler.adapter = BulletAdapter(this, callbackrv, type, fragmentName, this)
            linearLayout.addView(recycler)

        }

        if (callBacktv.data != "" || callbackrv.size > 0) {
            var isEdit: Boolean = false

            for (i in 0 until binding.texteditor.childCount) {
                val layout = binding.texteditor.getChildAt(i) as LinearLayout
                if (layout.tag.toString() == layoutid) {
//                    isEdit = false
//                    binding.texteditor.removeViewAt(i)
                }
            }
            linearLayout.tag = "layoutCount_$layoutcount"
            linearLayout.setOnClickListener(this)
            binding.texteditor.addView(linearLayout)
            val gson = Gson()

            val axixX = linearLayout.x
            val axixY = linearLayout.y

            val bulletArray = JSONArray()
            val bulletsJson = JSONObject()

            for (bullet in bulletList) {
                bulletArray.put(gson.toJson(bullet))
            }

            val titleString = gson.toJson(callBacktv)
            bulletsJson.put("axixY", axixY.toString())
            bulletsJson.put("axixX", axixX.toString())
            bulletsJson.put("layoutID", linearLayout.tag.toString())
            bulletsJson.put("type", type)
            bulletsJson.put("title", titleString)
            bulletsJson.put("bullet", bulletArray)

            if (!isEdit) {

                layoutcount++
            }
            this.bulletArray.put(bulletsJson)
            Log.d("data", this.bulletArray.toString())
        }
        binding.text.requestFocus()
    }


    override fun popuponclick(data: String, itemView: View, list: ArrayList<Styles>,position: Int) {
        //bullets Pop up
//        bulletList = list
//        openPopUp(data, itemView, "item")
    }

    private fun openDialog(position: Int, edit: Boolean, id: String) {

        var layoutid = id
        if (!edit) {
            styles = Styles()
            bulletList = ArrayList<Styles>()
            layoutid = ""

        }

        when (position) {
            TYPE_BULLETS -> {
                val checkBoxDiloag =
                    BulletDialog(
                        this, getString(R.string.Done), getString(R.string.cancel),
                        TYPE_BULLETS, this, fragmentName, styles, bulletList, layoutid
                    )
                checkBoxDiloag.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                checkBoxDiloag.show()
                binding.itemList.visibility = View.GONE
            }
            TYPE_CHECKBOX -> {
                val checkBoxDiloag =
                    BulletDialog(
                        this, getString(R.string.Done), getString(R.string.cancel),
                        TYPE_CHECKBOX, this, fragmentName, styles, bulletList, layoutid
                    )
                checkBoxDiloag.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                checkBoxDiloag.show()
                binding.itemList.visibility = View.GONE
            }
            TYPE_NUMBERLIST -> {
                val checkBoxDiloag =
                    BulletDialog(
                        this, getString(R.string.Done), getString(R.string.cancel),
                        TYPE_NUMBERLIST, this, fragmentName, styles, bulletList, layoutid
                    )
                checkBoxDiloag.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                checkBoxDiloag.show()
                binding.itemList.visibility = View.GONE
            }
            TYPE_ALPHALIST -> {
                val checkBoxDiloag =
                    BulletDialog(
                        this, getString(R.string.Done), getString(R.string.cancel),
                        TYPE_ALPHALIST, this, fragmentName, styles, bulletList, layoutid
                    )
                checkBoxDiloag.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                checkBoxDiloag.show()
                binding.itemList.visibility = View.GONE
            }

        }
    }


    companion object {

        private const val TYPE_BULLETS = 0
        private const val TYPE_CHECKBOX = 1
        private const val TYPE_NUMBERLIST = 2
        private const val TYPE_ALPHALIST = 3
        private const val INVALID_POINTER_ID = -1
        private fun adjustAngle(degrees: Float): Float {
            return when {
                degrees > 180.0f -> {
                    degrees - 360.0f
                }
                degrees < -180.0f -> {
                    degrees + 360.0f
                }
                else -> degrees
            }
        }

        private fun moveFinal(view: View, info: TransformInfo) {
            computeRenderOffset(view, info.pivotX, info.pivotY)
            adjustTranslation(view, info.deltaX, info.deltaY)
            var scale = view.scaleX * info.deltaScale
            scale = max(info.minimumScale, min(info.maximumScale, scale))
            view.scaleX = scale
            view.scaleY = scale
            val rotation = adjustAngle(view.rotation + info.deltaAngle)
            view.rotation = rotation

            val _info = info.toString()
            Log.d("info", _info)
        }

        private fun adjustTranslation(view: View, deltaX: Float, deltaY: Float) {
            val deltaVector = floatArrayOf(deltaX, deltaY)
            view.matrix.mapVectors(deltaVector)
            view.translationX = view.translationX + deltaVector[0]
            view.translationY = view.translationY + deltaVector[1]
        }

        private fun computeRenderOffset(view: View, pivotX: Float, pivotY: Float) {
            if (view.pivotX == pivotX && view.pivotY == pivotY) {
                return
            }
            val prevPoint = floatArrayOf(0.0f, 0.0f)
            view.matrix.mapPoints(prevPoint)
            view.pivotX = pivotX
            view.pivotY = pivotY
            val currPoint = floatArrayOf(0.0f, 0.0f)
            view.matrix.mapPoints(currPoint)
            val offsetX = currPoint[0] - prevPoint[0]
            val offsetY = currPoint[1] - prevPoint[1]
            view.translationX = view.translationX - offsetX
            view.translationY = view.translationY - offsetY
        }
    }

    override fun onMove(view: View, info: TransformInfo) {
       // super.onMove(view, info)

//        this.transinfo = info
//        if (listofTransform.size == 0) {
//            listofTransform.add(transinfo)
//        } else {
//            val contain = listofTransform.any {
//                it.viewTag == transinfo.viewTag
//            }
//            if (contain) {
//                for (i in 0 until listofTransform.size) {
//
//                    if (listofTransform[i].viewTag == transinfo.viewTag) {
//                        listofTransform.removeAt(i)
//                        listofTransform.add(i, transinfo)
//                        break
//
//                    }
//                }
//            } else {
//                listofTransform.add(transinfo)
//            }
//        }
    }

    private fun openRoundBottomSheet(pages: Int) {
        bottomSheetDialog = RoundedBottomSheetDialog(this)
        val bottomDialogView: View = LayoutInflater.from(this)
            .inflate(R.layout.bottom_dialog_pages, null)
        bottomSheetDialog.setContentView(bottomDialogView)

        var newPage: TextView
        var recyclerView: RecyclerView
        var noPage: TextView
        var indexlayout: ConstraintLayout

        indexlayout = bottomDialogView.findViewById(R.id.indexlayout)
        noPage = bottomDialogView.findViewById(R.id.nopage)
        newPage = bottomDialogView.findViewById<TextView>(R.id.newPage)
        recyclerView = bottomDialogView.findViewById(R.id.rv_pages)
        newPage.visibility = View.GONE

        indexlayout.visibility = View.VISIBLE

        indexlayout.setOnClickListener {
            this.finish()
        }

        var list: ArrayList<Int> = ArrayList()
        if (pages > 0)
            for (i in 0 until pages) {
                list.add(i)
            }
        else {
            noPage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            newPage.visibility = View.GONE
        }

        var _adapter = PagesAdapter(this, list, object : iOnClickListerner {
            override fun onclick(position: Int) {
                super.onclick(position)
                var pos = position.plus(1)
                startActivity(
                    Intent(this@ShowPage, ShowPage::class.java).putExtra(
                        "Page",
                        pos
                    )
                )
                this@ShowPage.finish()

                bottomSheetDialog.dismiss()
            }
        })
        recyclerView.adapter = _adapter

        newPage.setOnClickListener(View.OnClickListener { view1: View? ->
            var bundle = Bundle()
            var page = pages.plus(1)
            bundle.putInt("pageid", page)


            bottomSheetDialog.dismiss()

        })

        bottomSheetDialog.show()


    }

}