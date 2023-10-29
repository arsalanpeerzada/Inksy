package com.inksy.UI.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Html
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.burhanrashid52.photoediting.*
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.inksy.Database.Entities.PageTable
import com.inksy.Database.JournalDatabase
import com.inksy.Interfaces.OnDialogBulletClickListener
import com.inksy.Interfaces.PopUpOnClickListerner
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Interfaces.onMoveListener
import com.inksy.Model.Styles
import com.inksy.Model.TransformInfo
import com.inksy.R
import com.inksy.UI.Adapter.BulletAdapter
import com.inksy.UI.Adapter.BulletSelectAdapter
import com.inksy.UI.Adapter.PagesAdapter
import com.inksy.UI.Constants
import com.inksy.UI.Dialogs.BulletDialog
import com.inksy.Utils.FileUtil
import com.inksy.Utils.TinyDB
import com.inksy.databinding.ActivityShowJournalBinding
import io.github.hyuwah.draggableviewlib.DraggableView
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.TextStyleBuilder
import ja.burhanrashid52.photoeditor.ViewType
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder
import ja.burhanrashid52.photoeditor.shape.ShapeType
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

class ShowJournal : AppCompatActivity(), iOnClickListerner, OnPhotoEditorListener,
    PropertiesBSFragment.Properties, ShapeBSFragment.Properties, EmojiBSFragment.EmojiListener,
    StickerBSFragment.StickerListener, OnDialogBulletClickListener, PopUpOnClickListerner,
    View.OnFocusChangeListener, View.OnClickListener, onMoveListener {
    lateinit var tinyDB: TinyDB
    lateinit var transinfo: TransformInfo
    var imageArray: JSONArray = JSONArray()
    var textArray: JSONArray = JSONArray()
    var bulletArray: JSONArray = JSONArray()
    lateinit var llTestDraggable: DraggableView<LinearLayout>
    private var layoutDragActive = " "
    private var layoutcount: Int = 0
    var fragmentName = Constants.CREATEJOURNALINDEX
    private var mShapeBSFragment: ShapeBSFragment? = null
    private var mShapeBuilder: ShapeBuilder? = null
    private val PICK_IMAGE_BACKGROUND = 2
    private lateinit var cameraUri: Uri

    lateinit var mPhotoEditor: PhotoEditor
    private val CAMERA_REQUEST = 52
    private val PICK_REQUEST = 53
    val PINCH_TEXT_SCALABLE_INTENT_KEY = "PINCH_TEXT_SCALABLE"
    var coverTitle = ""
    var categoryId = 0
    var coverDesciption = ""
    var categoryname = ""
    lateinit var camerauri: Uri
    private lateinit var bottomSheetDialog: RoundedBottomSheetDialog
    lateinit var styles: Styles
    lateinit var bulletList: ArrayList<Styles>
    lateinit var journalDatabase: JournalDatabase
    private var selectedFile: File? = null
    lateinit var binding: ActivityShowJournalBinding
    var createdby = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var jsonString = ""
        var backgroundImage: String = ""

        if (intent.extras != null) {
            jsonString = intent.getStringExtra("JSON").toString()
            backgroundImage = intent.getStringExtra("background").toString()
            createdby = intent.getIntExtra("createdby",0)

        }
        binding.loader.visibility = View.VISIBLE

        Handler().postDelayed({

            binding.loader.visibility = View.GONE
        }, 2000)
        bulletArray = JSONArray()

        init()
        initClickListener()
        journalDatabase = JournalDatabase.getInstance(this)!!

        binding.photoEditorView.background =
            ContextCompat.getDrawable(this, R.drawable.whiteimage)


        if (backgroundImage.isNullOrEmpty()) {

            if (jsonString != null && jsonString != "") {
                var jsonObject = JSONObject(jsonString)
                var indexBackground = jsonObject.getString("IndexBackground")
                bulletArray = jsonObject.getJSONArray("ArrayofBullets")
                textArray = jsonObject.getJSONArray("ArrayofText")
                imageArray = jsonObject.getJSONArray("ArrayofImage")
                var pageJSONArray = jsonObject.getJSONArray("Pages")


                var gson = Gson()
                var index = bulletArray.length()
                var textarray = textArray.length()
                var imagearray = imageArray.length()
                var pageArray = pageJSONArray.length()

                journalDatabase.getJournalData().deleteTable()
                journalDatabase.getJournalData().deletepageforlinkTable()
                if (pageJSONArray.length() > 0) {

                    binding.nextpage.visibility = View.VISIBLE
                    for (i in 0 until pageArray) {

                        var jsonOb =
                            gson.fromJson(pageJSONArray[i].toString(), PageTable::class.java)

                        lifecycleScope.launch {

                            journalDatabase.getJournalData().insertPage(
                                jsonOb
                            )
                        }
                        Log.d("Tag", jsonOb.toString())
                    }
                } else {
                    binding.nextpage.visibility = View.GONE
                }


                if (indexBackground.isNullOrEmpty()) {

                } else {
                    Glide.with(this)
                        .asBitmap()
                        .load(Constants.BASE_IMAGE + indexBackground)
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

                for (i in 0 until index) {
                    var jsonObject = bulletArray.getJSONObject(i)
                    var title = jsonObject.getString("title")
                    var bulletList = jsonObject.get("bullet") as JSONArray
                    var type = jsonObject.getInt("type")
                    var axixX = jsonObject.getString("axixX")
                    var axixY = jsonObject.getString("axixY")
                    var layoutName = jsonObject.getString("layoutID")
                    val titleModel: Styles = gson.fromJson(title, Styles::class.java)

                    var index = bulletList.length()

                    var bullets: ArrayList<Styles> = ArrayList()
                    for (j in 0 until index) {
                        var item = bulletList.getString(j)
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
                for (k in 0 until textarray) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        setTextArray(k)
                    }
                }
                for (l in 0 until imagearray) {

                    val imageJsonObject = imageArray.getJSONObject(l)
                    var width: String = imageJsonObject.getString("width")
                    var height: String = imageJsonObject.getString("height")
                    var bit = imageJsonObject.getString("bitmap")
                    var axixX = imageJsonObject.getString("axixX")
                    var axixY = imageJsonObject.getString("axixY")
                    var info = imageJsonObject.getString("info")
                    val viewtag = imageJsonObject.getString("viewtag")


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
                                            moveFinal(view, _info)
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

            styles = Styles()
            bulletList = ArrayList()

            mPhotoEditor.clearHelperBox()

            mShapeBSFragment = ShapeBSFragment()
            mShapeBSFragment!!.setPropertiesChangeListener(this)

        } else {
            Glide.with(this)
                .asBitmap()
                .load(Constants.BASE_IMAGE + backgroundImage)
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

        binding.home.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            this.finish()
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
        var width: String = jsonObject.getString("width")
        var height: String = jsonObject.getString("height")
        var info: String = jsonObject.getString("info")
        var viewtag: String = jsonObject.getString("viewtag")


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
            false
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
//        mPhotoEditor.setOnPhotoEditorListener(this)


        //Set Image Dynamically
        binding.photoEditorView.background =
            ContextCompat.getDrawable(this, R.drawable.paris_tower)
        binding.photoEditorView.source!!.setImageResource(R.drawable.transparentdraw)
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
            onBackPressed()
        }
        binding.checked.setOnClickListener {
//            val action = CreateJournalIndexDirections.actionCreateJournalIndexToCreateJournalEntry()
//            findNavController().navigate(action)
//            sendJson()
            var pages = journalDatabase.getJournalData().getAllPages()

            var pagerows = pages.size


            openRoundBottomSheet(pagerows)

        }

        binding.nextpage.setOnClickListener {

            var pages = journalDatabase.getJournalData().getAllPages()
            if (pages.isNotEmpty()) {
                startActivity(
                    Intent(this@ShowJournal, ShowPageViewPager::class.java).putExtra("createdby",createdby)
                )
                overridePendingTransition(R.anim.left, R.anim.left2);
            } else {
                Toast.makeText(this, "There are No pages to show", Toast.LENGTH_SHORT).show()
            }

        }
        binding.backPage.setOnClickListener{
            this.finish()
        }

        binding.picture.setOnClickListener {
            //Add drawable sticker

            val intent2 = Intent()
            intent2.type = "image/*"
            intent2.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent2, "Select Picture"), PICK_REQUEST
            )
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
////                    val action =
////                        CreateJournalIndexDirections.actionCreateJournalIndexToCreateJournalEntry()
////                    findNavController().navigate(action)
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
                val ran = (1..1000).random()
                var viewtag = "IMAGE_$ran"
                mPhotoEditor.addImage(bitmap, 0f, 0f, 0f, 0f, viewtag, this, false)

                val input = contentResolver?.openInputStream(cameraUri)
                val image = BitmapFactory.decodeStream(input, null, null)

                // Encode image to base64 string
                val baos = ByteArrayOutputStream()
                image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                var imageBytes = baos.toByteArray()
                val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)


                var json = JSONObject()
                json.put("bitmap", imageString)
                json.put("axixX", "0")
                json.put("axixY", "0")
                json.put("viewtag", viewtag)

                imageArray.put(json)

            } else if (requestCode == PICK_IMAGE_BACKGROUND) {
                cameraUri = data!!.data!!

                val uri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                    this.applicationContext.contentResolver,
                    uri
                )
                binding.photoEditorView.source.setImageBitmap(bitmap)


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
//        Toast.makeText(this, "View Added", Toast.LENGTH_SHORT).show()
    }

    override fun onRemoveViewListener(
        viewType: ViewType?,
        numberOfAddedViews: Int,
        tag: String
    ) {

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

                var jsonObject = JSONObject()
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
        toFloat: Float,
        toFloat1: Float
    ) {

        val linearLayout = LinearLayout(this)
        linearLayout.setPadding(40)
        var linearlayoutparams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.layoutParams = linearlayoutparams
        linearLayout.orientation = LinearLayout.VERTICAL

        var x = toFloat
        var y = toFloat1
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
//                if (layout.tag.toString() == layoutid) {
//                    isEdit = false
//                    binding.texteditor.removeViewAt(i)
//                }
            }
            linearLayout.tag = "layoutCount_$layoutcount"
            linearLayout.setOnClickListener(this)
            binding.texteditor.addView(linearLayout)
            var gson = Gson()

            var axixX = linearLayout.x
            var axixY = linearLayout.y
            var axixZ = linearLayout.z

            var bulletArray = JSONArray()
            var bulletsJson = JSONObject()

            for (bullet in bulletList) {
                bulletArray.put(gson.toJson(bullet))
            }

            var titleString = gson.toJson(callBacktv)
            bulletsJson.put("axixY", axixY.toString())
            bulletsJson.put("axixX", axixX.toString())
            bulletsJson.put("axixZ", axixZ.toString())
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


    override fun popuponclick(
        data: String,
        itemView: View,
        list: ArrayList<Styles>,
        position: Int
    ) {
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

    private fun sendJson() {
        val layout = binding.texteditor
        val child = layout.childCount
        val editor = binding.photoEditorView
        val editorchildren = editor.childCount
        var i = 0
        var textarrayCount = 0
        var imageArrayCount = 0
        for (a in 3 until editorchildren) {

            var _data = editor.getChildAt(a) as FrameLayout
            val centreX = (_data.x).toString()
            val centreY = (_data.y).toString()
            var tag = _data.tag.toString()

            var height = _data.height.toString()
            var width = _data.width.toString()

            var gson = Gson()

//
//            _data.rotationX = 20f
//            _data.rotationY = 20f

            var _info = gson.toJson(transinfo)
            if (tag == "TEXT") {
                var textObject = textArray.getJSONObject(textarrayCount)
                textObject.put("axixX", centreX)
                textObject.put("axixY", centreY)
                textObject.put("width", width)
                textObject.put("height", height)
                textArray.put(textarrayCount, textObject)
                textarrayCount += 1


            } else if (tag == "IMAGE") {
                val imagejson = imageArray.getJSONObject(imageArrayCount)
                imagejson.put("axixX", centreX)
                imagejson.put("axixY", centreY)
                imagejson.put("width", width)
                imagejson.put("height", height)
                imagejson.put("info", _info)
                imageArray.put(imageArrayCount, imagejson)
                imageArrayCount += 1
            }


        }

        for (i in 0 until child) {
            var _data = layout.getChildAt(i) as? LinearLayout

            val centreX = (_data?.x!!).toString()
            val centreY = (_data.y).toString()


            var jsonObject = bulletArray.getJSONObject(i)
            jsonObject.put("axixX", centreX)
            jsonObject.put("axixY", centreY)
            bulletArray.put(i, jsonObject)

        }

        var array: JSONObject = JSONObject()
        array.put("ArrayofBullets", bulletArray)
        array.put("ArrayofText", textArray)
        array.put("ArrayofImage", imageArray)

        var arraystring = array.toString()

        saveJournal(arraystring)

//        var tinyDB = TinyDB(this)
//        tinyDB.putString("jsondata", arraystring)
//        openPopUp("Edit", binding.plus, "Edit")
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
            var _info = info.toString()
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
//        super.onMove(view, info)
//        this.transinfo = info
    }


    private fun saveJournal(arraystring: String) {

//        lateinit var jouralView: JournalView
//        jouralView = ViewModelProvider(this)[JournalView::class.java]
//        jouralView.init()
//
//
//        var file: File = FileUtil.from(this, cameraUri)
//
//        var token = tinyDB.getString("token")
//        jouralView.journalCreate(
//            token!!,
//            categoryId,
//            coverTitle,
//            "blue",
//            coverDesciption,
//            arraystring,
//            "0",
//            "1",
//            file
//        )?.observe(this) {
//            when (it.status) {
//                Status.SUCCESS -> {}
//                Status.ERROR -> {}
//                Status.LOADING -> {}
//
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

        indexlayout.visibility = View.GONE

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
                    Intent(this@ShowJournal, ShowPage::class.java).putExtra(
                        "Page",
                        pos
                    )
                )

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