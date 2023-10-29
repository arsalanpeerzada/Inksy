package com.inksy.UI.Fragments

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
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.burhanrashid52.photoediting.*
import com.burhanrashid52.photoediting.TextEditorDialogFragment.Companion.show
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog
import com.elconfidencial.bubbleshowcase.BubbleShowCase
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.inksy.Database.Entities.JournalIndexTable
import com.inksy.Database.Entities.PageTable
import com.inksy.Database.Entities.PurchasedDoodles
import com.inksy.Database.Entities.SelectedAudience
import com.inksy.Database.JournalDatabase
import com.inksy.Interfaces.*
import com.inksy.Model.Styles
import com.inksy.Model.TransformInfo
import com.inksy.R
import com.inksy.Remote.Status
import com.inksy.UI.Activities.DoodleStore
import com.inksy.UI.Activities.MainActivity
import com.inksy.UI.Adapter.BulletAdapter
import com.inksy.UI.Adapter.BulletSelectAdapter
import com.inksy.UI.Adapter.DoodlePurchasedAdapter
import com.inksy.UI.Adapter.PagesAdapter
import com.inksy.UI.Constants
import com.inksy.UI.Dialogs.BulletDialog
import com.inksy.UI.Dialogs.TwoButtonDialog
import com.inksy.UI.ViewModel.JournalView
import com.inksy.Utils.FileUtil
import com.inksy.Utils.TinyDB
import com.inksy.databinding.FragmentCreateJournalIndexBinding
import io.github.hyuwah.draggableviewlib.DraggableView
import ja.burhanrashid52.photoeditor.*
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder
import ja.burhanrashid52.photoeditor.shape.ShapeType
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import kotlin.math.max
import kotlin.math.min


class CreateJournalIndex : Fragment(), iOnClickListerner, OnPhotoEditorListener,
    PropertiesBSFragment.Properties, ShapeBSFragment.Properties, EmojiBSFragment.EmojiListener,
    StickerBSFragment.StickerListener, OnDialogBulletClickListener, PopUpOnClickListerner,
    View.OnFocusChangeListener, View.OnClickListener, onMoveListener {

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
    lateinit var binding: FragmentCreateJournalIndexBinding
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
    lateinit var journalDatabase: JournalDatabase
    var backgroundImage = ""
    var token = ""
    var gson: Gson = Gson()
    lateinit var indexdate: List<JournalIndexTable>
    lateinit var data: JournalIndexTable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCreateJournalIndexBinding.inflate(layoutInflater)

        tinyDB = TinyDB(requireContext())
        token = tinyDB.getString("token").toString()
        transinfo = TransformInfo(0f, 0f, 0f, 0f, 0f, 0f, 0f, 10f)
        journalDatabase = JournalDatabase.getInstance(requireContext())!!

        bulletArray = JSONArray()
        init()
        initClickListener()
        jouralView = ViewModelProvider(requireActivity())[JournalView::class.java]
        jouralView.init()

        indexdate = journalDatabase.getJournalData().getAllNotes()


        data = indexdate[0]
        binding.photoEditorView.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.whiteimage)

//        journalDatabase.getJournalData()
//            .UpdateIndexCover(data.indexTemplate!!, "1")

        if (!data.indexTemplate.isNullOrEmpty()) {
            Glide.with(this@CreateJournalIndex)
                .asBitmap()
                .load(Constants.BASE_IMAGE + data.indexTemplate)
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
        } else if (!data.indexBackground.isNullOrEmpty()) {
            Glide.with(this)
                .asBitmap()
                .load(Constants.BASE_IMAGE + data.indexBackground)
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


        if (data != null && indexdate != null) {

            val gson = Gson()

            if (data.htmlContent != null && data.htmlContent != "") {
                var jsonObject = JSONObject(data.htmlContent)
                var pageJSONArray = jsonObject.getJSONArray("Pages")
                var pageArray = pageJSONArray.length()
//                journalDatabase.getJournalData().deleteTable()

                var dblength = journalDatabase.getJournalData().getAllPages()
                if (dblength.size < pageArray)
                    if (pageJSONArray.length() > 0) {

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
                    }
            }

            if (data.arrayOfBullets != null && data.arrayOfBullets != "[]" && data.arrayOfBullets != "") {
                bulletArray = JSONArray(data.arrayOfBullets)
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
            if (data.arrayOfText != null && data.arrayOfText != "[]" && data.arrayOfText != "") {
                textArray = JSONArray(data.arrayOfText)
                val textarray = textArray.length()
                for (k in 0 until textarray) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        setTextArray(k)
                    }
                }
            }

            if (data.arrayOfImage != null && data.arrayOfImage != "[]" && data.arrayOfImage != "") {
                imageArray = JSONArray(data.arrayOfImage)
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
                                    viewtag,
                                    object : onMoveListener {
                                        override fun onMove(view: View, info: TransformInfo) {
                                            super.onMove(view, info)
                                            transinfo = info

                                            if (listofTransform.size == 0) {
                                                listofTransform.add(transinfo)
                                            } else {
                                                var contain = listofTransform.any {
                                                    it.viewTag == transinfo.viewTag
                                                }
                                                if (contain) {
                                                    for (i in 0 until listofTransform.size) {

                                                        if (listofTransform[i].viewTag == transinfo.viewTag) {
                                                            listofTransform.removeAt(i)
                                                            listofTransform.add(i, transinfo)
                                                            break

                                                        }
                                                    }
                                                } else {
                                                    listofTransform.add(transinfo)
                                                }
                                            }


                                        }
                                    },
                                    true
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
                                            listofTransform.add(_info)
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
        }
        styles = Styles()
        bulletList = ArrayList()
        mShapeBSFragment = ShapeBSFragment()
        mShapeBSFragment!!.setPropertiesChangeListener(this)
        var closeImage: Drawable = resources.getDrawable(R.drawable.baseline_close_16)

        var bulletShowcase = BubbleShowCaseBuilder(requireActivity()) //Activity instance
            .title("Bullets")
            .description("In this option you can select four types of bullets, then you can select Header, add bullet items, bold, italic, underline, and strike through text, increase and decrease text size and change typeface of text")//Any title for the bubble view
            .backgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            .textColor(ContextCompat.getColor(requireContext(), R.color.black))
            .targetView(binding.bullets) //View to point out
        //Display the ShowCase

        var pictureShowcase = BubbleShowCaseBuilder(requireActivity()) //Activity instance
            .title("Picture")
            .description("Add your photos or doodles you got from the doodle store.")//Any title for the bubble view
            .backgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            .textColor(ContextCompat.getColor(requireContext(), R.color.black))
            .targetView(binding.picture) //View to point out
            .closeActionImage(closeImage)
        //Display the ShowCase

        var drawwingShowcase = BubbleShowCaseBuilder(requireActivity()) //Activity instance
            .title("Drawing")
            .description("Draw something with the pen.")//Any title for the bubble view
            .backgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            .textColor(ContextCompat.getColor(requireContext(), R.color.black))
            .targetView(binding.doodle) //View to point out
            .closeActionImage(closeImage)
        //Display the ShowCase


        var textShowcase = BubbleShowCaseBuilder(requireActivity()) //Activity instance
            .title("Text")
            .description("Add some text to your journal.")//Any title for the bubble view
            .backgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            .textColor(ContextCompat.getColor(requireContext(), R.color.black))
            .targetView(binding.text) //View to point out
            .closeActionImage(closeImage)
        //Display the ShowCase

        var undoShowcase = BubbleShowCaseBuilder(requireActivity()) //Activity instance
            .title("Undo")
            .description("")//Any title for the bubble view
            .backgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            .textColor(ContextCompat.getColor(requireContext(), R.color.black))
            .targetView(binding.buttonUndo) //View to point out
            .closeActionImage(closeImage)

        var redoShowcase = BubbleShowCaseBuilder(requireActivity()) //Activity instance
            .title("Redo")
            .description("")//Any title for the bubble view
            .backgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            .textColor(ContextCompat.getColor(requireContext(), R.color.black))
            .targetView(binding.buttonRedo) //View to point out
            .closeActionImage(closeImage)

        var pageShowcase = BubbleShowCaseBuilder(requireActivity()) //Activity instance
            .title("Page")
            .description("Create a new journal page.")//Any title for the bubble view
            .backgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            .textColor(ContextCompat.getColor(requireContext(), R.color.black))
            .targetView(binding.plus) //View to point out
            .closeActionImage(closeImage)


        var firstTime = tinyDB.getBoolean("isfirstTime")
        if (firstTime) {
            tinyDB.putBoolean("isfirstTime", false)


            //.addShowCase(bulletShowcase)
            BubbleShowCaseSequence()
                .addShowCase(pictureShowcase)
                .addShowCase(drawwingShowcase)
                .addShowCase(textShowcase)
                .addShowCase(undoShowcase)
                .addShowCase(redoShowcase)
                .addShowCase(pageShowcase)
                .show()
        }

        return binding.root
    }

    private fun onUserClicked(s: String?) {

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
        val info: String = jsonObject.getString("info")

        val styleBuilder = TextStyleBuilder()
        styleBuilder.withTextColor(colorCode)
        styleBuilder.withTextSize(30f)

        if (_typface != 0) {
            val typeface = context?.resources?.getFont(_typface)
            styleBuilder.withTextFont(typeface!!)
        }

        if (isBold && isItalic) {
            styleBuilder.withTextStyle(Typeface.BOLD_ITALIC)

        } else if (isBold) {
            styleBuilder.withTextStyle(Typeface.BOLD)

        } else if (isItalic) {
            styleBuilder.withTextStyle(Typeface.ITALIC)
        }

        mPhotoEditor.addText(
            inputText,
            styleBuilder,
            axixX.toFloat(),
            axixY.toFloat(),
            width.toFloat(),
            height.toFloat(),
            viewtag,
            this,
            true,
        )

        if (info != "") {
            val gson = Gson()
            val _info: TransformInfo =
                gson.fromJson(info, TransformInfo::class.java)
            for (l in 3 until binding.photoEditorView.childCount) {
                val view =
                    binding.photoEditorView.getChildAt(l) as FrameLayout


                val mtag = view.tag
                if (mtag == _info.viewTag) {
                    listofTransform.add(_info)
                    moveFinal(view, _info)
                }

            }
        }
    }


    private fun showBottomSheetDialogFragment(fragment: BottomSheetDialogFragment?) {
        if (fragment == null || fragment.isAdded) {
            return
        }
        fragment.show(childFragmentManager, fragment.tag)
    }

    fun init() {
        val pinchTextScalable =
            requireActivity().intent.getBooleanExtra(PINCH_TEXT_SCALABLE_INTENT_KEY, true)


        //Typeface mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");
        mPhotoEditor = PhotoEditor.Builder(requireContext(), binding.photoEditorView)
            .setPinchTextScalable(pinchTextScalable) // set flag to make text scalable when pinch
            //.setDefaultTextTypeface(mTextRobotoTf)
            //.setDefaultEmojiTypeface(mEmojiTypeFace)
            .build() // build photo editor sdk
        mPhotoEditor.setOnPhotoEditorListener(this)

        //Set Image Dynamically

        binding.photoEditorView.source!!.setImageResource(R.drawable.transparentdraw)
        binding.photoEditorView.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.paris_tower)
    }

    private fun initClickListener() {
        binding.bullets.setOnClickListener {
            val array = arrayOf(
                R.drawable.bullets_list, R.drawable.checkbox,
                R.drawable.numbers_list, R.drawable.alphabetic
            )

            binding.itemList.adapter = BulletSelectAdapter(requireContext(), array, this)

            if (binding.itemList.visibility == View.VISIBLE) {
                binding.itemList.visibility = View.GONE
            } else {
                binding.itemList.visibility = View.VISIBLE


            }
        }
        binding.ivBack.setOnClickListener {


            openDialog()
        }
        binding.checked.setOnClickListener {

            //  binding.checked.isEnabled = false
            sendJson("apiHit")
        }

        binding.picture.setOnClickListener {
            //Add drawable sticker


            val contextWrapper = ContextThemeWrapper(requireContext(), R.style.popupMenuStyle)
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
                        openDoodle()
                        return@OnMenuItemClickListener true
                    }

                    else -> false
                }


            })
            popupMenu.inflate(R.menu.view_journal_popup)
            popupMenu.show()
            popupMenu.menu.findItem(R.id.Home).isVisible = false
            popupMenu.menu.findItem(R.id.edit).isVisible = false
            popupMenu.menu.findItem(R.id.Delete).isVisible = false
            popupMenu.menu.findItem(R.id.View).isVisible = false
            popupMenu.menu.findItem(R.id.Report).isVisible = false
            popupMenu.menu.findItem(R.id.block).isVisible = false


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

            sendJson("nextPage")
            var pages = journalDatabase.getJournalData().getAllPages()

            var pagerows = pages.size


            openRoundBottomSheet(pagerows)

        }
    }

    private fun openDialog() {
        val twoButtonDialog: TwoButtonDialog = TwoButtonDialog(
            requireContext(), "Discard Journal",
            "Are you sure, You want to discard this journal?",
            getString(android.R.string.yes),
            getString(android.R.string.no),
            object : OnDialogClickListener {
                override fun onDialogClick(callBack: String?) {
                    if (callBack == "Yes") {
                        requireActivity().finish()
                    } else {
                    }
                }
            })
        twoButtonDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        twoButtonDialog.show()
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {

        Log.d("ViewCheck", v.toString())
    }

    override fun onClick(v: View?) {

        if (layoutDragActive == v?.tag) {
            val layout = v as LinearLayout

            llTestDraggable.disableDrag()
            layoutDragActive = ""
            var data = layout.background
            layout.setBackgroundResource(R.color.transparent)

        } else {
            val layout = v as LinearLayout
            layout.setBackgroundResource(R.drawable.border_layout_grey)

            layoutDragActive = v.tag.toString()

            llTestDraggable = DraggableView.Builder(layout)
                .setStickyMode(DraggableView.Mode.NON_STICKY)
                .build()
        }

        for (i in 0 until binding.texteditor.size) {

            val layout = binding.texteditor.getChildAt(i)

            if (layoutDragActive != layout.tag.toString()) {
                layout.setBackgroundResource(R.color.transparent)


            }
        }
    }

    private fun openPopUp(data: String, itemView: View, layoutType: String, position: Int) {
        val contextWrapper = ContextThemeWrapper(requireContext(), R.style.popupMenuStyle)
        val popupMenu = PopupMenu(
            contextWrapper, itemView
        )
        popupMenu.setForceShowIcon(true)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.link_title -> {

                    var _data = data
                    var _itemView = itemView
                    var _layout = layoutType


                    val linearLay = itemView.parent as LinearLayout
                    var idddd = linearLay.tag.toString()
                    // Saving data to Room for Next Page
                    sendJson("nextPage")

                    val bundle = Bundle()
                    bundle.putBoolean(Constants.BULLETITEMCHECK, true)
                    bundle.putInt("bulletpageid", Constants.LINKFORHEADER)
                    bundle.putString("bulletlayoutId", idddd)

                    findNavController().navigate(
                        R.id.action_createJournalIndex_to_create_journal_entry,
                        bundle
                    )
                    return@OnMenuItemClickListener true
                }

                R.id.link_bullet -> {

                    var _data = data
                    var _itemView = itemView
                    var _layout = layoutType
                    // Saving data to Room for Next Page

                    val recyclerView = itemView.parent as RecyclerView
                    val layout = recyclerView.parent as LinearLayout
                    var idddd = layout.tag.toString()

                    sendJson("nextPage")

                    val bundle = Bundle()
                    bundle.putBoolean(Constants.BULLETITEMCHECK, true)
                    bundle.putInt("bulletpageid", position)
                    bundle.putString("bulletlayoutId", idddd)


                    findNavController().navigate(
                        R.id.action_createJournalIndex_to_create_journal_entry,
                        bundle
                    )
                    return@OnMenuItemClickListener true
                }

                R.id.Edit -> {

// Get Data from Bullet Layout and Open the dialog
                    var id: String = ""
                    if (layoutType == "item") {
                        val recyclerView = itemView.parent as RecyclerView
                        val layout = recyclerView.parent as LinearLayout
                        id = layout.tag.toString()
                        var layoutid: String = ""
                        var _jsonObject: JSONObject = JSONObject()
                        for (z in 0 until bulletArray.length()) {
                            _jsonObject = bulletArray.getJSONObject(z)
                            layoutid = _jsonObject.getString("layoutID")

                        }
//                        val textview = layout.getChildAt(1) as TextView
                        if (layoutid == id) {
                            var style = _jsonObject.getString("title")
                            var gson = Gson()
                            var styleed = gson.fromJson(style, Styles::class.java)
                            styles = styleed
                        }


                    } else {
                        val textview = itemView as TextView
                        val layout = textview.parent as LinearLayout
                        id = layout.tag.toString()
                    }

                    openDialog(data.toInt(), true, id)

                    return@OnMenuItemClickListener true
                }

                R.id.Delete -> {
                    val textview = itemView as TextView
                    val layout = textview.parent as LinearLayout
                    var tag = layout.tag
                    binding.texteditor.removeView(layout)

                    for (i in 0 until bulletArray.length()) {
                        val bulletObject = bulletArray.getJSONObject(i)
                        var id = bulletObject.get("layoutID")

                        if (id == tag) {
                            bulletArray.remove(i)
                            break
                        }
                    }

                    return@OnMenuItemClickListener true
                }

                else -> false
            }
        })
        popupMenu.inflate(R.menu.pop_up)
        popupMenu.show()

        popupMenu.menu.findItem(R.id.Delete).isVisible = false
        popupMenu.menu.findItem(R.id.link_bullet).isVisible = false
        popupMenu.menu.findItem(R.id.Edit).isVisible = true
        popupMenu.menu.findItem(R.id.link_title).isVisible = false
        if (data == "Edit" || data == "title") {
            popupMenu.menu.findItem(R.id.link_bullet).isVisible = false
            popupMenu.menu.findItem(R.id.link_title).isVisible = false
            popupMenu.menu.findItem(R.id.Edit).isVisible = false
            popupMenu.menu.findItem(R.id.Delete).isVisible = true
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
                    requireActivity().applicationContext.contentResolver,
                    uri
                )

                val file: File = FileUtil.from(requireContext(), cameraUri)

                jouralView.imageUpload(token, file)?.observe(requireActivity()) {
                    when (it?.status) {
                        Status.SUCCESS -> {
                            val ran = (1..1000).random()
                            val viewtag = "IMAGE_$ran"
                            mPhotoEditor.addImage(bitmap, 0f, 0f, 0f, 0f, viewtag, this, true)
                            val json = JSONObject()
                            json.put("bitmap", it?.data?.data?.avatar.toString())
                            json.put("axixX", "0")
                            json.put("axixY", "0")
                            json.put("viewtag", viewtag)
                            imageArray.put(json)
                        }

                        Status.ERROR -> {}
                        Status.LOADING -> {}
                        else -> {}
                    }
                }

            } else if (requestCode == PICK_IMAGE_BACKGROUND) {


                cameraUri = data!!.data!!

                val uri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().applicationContext.contentResolver,
                    uri
                )
                val d: Drawable = BitmapDrawable(resources, bitmap)
                val file: File = FileUtil.from(requireContext(), cameraUri)
                jouralView.imageUpload(token, file)?.observe(requireActivity()) {
                    when (it?.status) {
                        Status.SUCCESS -> {
                            binding.photoEditorView.setBackgroundDrawable(d)
                            backgroundImage = it.data?.data?.avatar.toString()

                            journalDatabase.getJournalData()
                                .UpdateIndexCover(backgroundImage!!, "1")
                        }

                        Status.ERROR -> {}
                        Status.LOADING -> {}
                        else -> {}
                    }
                }


            } else if (requestCode == CAMERA_REQUEST) {
                val selectedFilePath: String = FileUtil.getPath(requireContext(), cameraUri)
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
        // Toast.makeText(requireContext(), "View Added", Toast.LENGTH_SHORT).show()
    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int, tag: String) {
        val layout = binding.texteditor
        val child = layout.childCount
        val editor = binding.photoEditorView
        val editorchildren = editor.childCount

        var textarrayCount = 0
        var imageArrayCount = 0

        for (a in 3 until editorchildren) {

            if (tag.contains("TEXT")) {
                val textObject = textArray.getJSONObject(textarrayCount)
                val texttag = textObject.get("viewtag")

                if (tag == texttag) {
                    textArray.remove(textarrayCount)
                    break
                }
                textarrayCount++


            } else if (tag.contains("IMAGE")) {
                val imagejson = imageArray.getJSONObject(imageArrayCount)
                val texttag = imagejson.get("viewtag")

                if (tag == texttag) {
                    imageArray.remove(imageArrayCount)
                    break;
                }
                imageArrayCount++

            }
        }
    }

    override fun onStartViewChangeListener(viewType: ViewType?) {
        //  Toast.makeText(requireContext(), "View Change", Toast.LENGTH_SHORT).show()
    }

    override fun onStopViewChangeListener(viewType: ViewType?) {
        //  Toast.makeText(requireContext(), "View Change Stop", Toast.LENGTH_SHORT).show()
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
        val textEditorDialogFragment = show(requireActivity() as AppCompatActivity)
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
                    val typeface = context?.resources?.getFont(_typface)
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
                val viewtag = "TEXT_$ran"

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
                            super.onMove(view, info)
                            transinfo = info

                            if (listofTransform.size == 0) {
                                listofTransform.add(transinfo)
                            } else {
                                val contain = listofTransform.any {
                                    it.viewTag == transinfo.viewTag
                                }
                                if (contain) {
                                    for (i in 0 until listofTransform.size) {

                                        if (listofTransform[i].viewTag == transinfo.viewTag) {
                                            listofTransform.removeAt(i)
                                            listofTransform.add(i, transinfo)
                                            break

                                        }
                                    }
                                } else {
                                    listofTransform.add(transinfo)
                                }
                            }


                        }
                    }, true
                )
            }
        })
    }


    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
        val oldtext = text
        val mtag = rootView?.tag.toString()

        val textEditorDialogFragment =
            show(requireActivity() as AppCompatActivity, text!!, colorCode)
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
                    val typeface = context?.resources?.getFont(_typface)
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

                jsonObject.put("axixX", 0f)
                jsonObject.put("axixY", 0f)
                jsonObject.put("viewtag", mtag)

                for (i in 0 until textArray.length()) {

                    val json = textArray.getJSONObject(i)
                    val text = json.getString("text")

                    if (oldtext == text) {
                        textArray.remove(i)
                        textArray.put(jsonObject)
                        break
                    }
                }
                if (_typface != 0) {
                    val typeface = context?.resources?.getFont(_typface)
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

        val linearLayout = LinearLayout(requireContext())
        linearLayout.setPadding(40)
        var linearlayoutparams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.layoutParams = linearlayoutparams
        linearLayout.orientation = LinearLayout.VERTICAL
        var idcount = 0
        var x = _axixX
        var y = _axixY
        linearLayout.x = x
        linearLayout.y = y

        var rv = callbackrv
        if (callBacktv.data != "") {

            binding.texteditor.requestFocus()
            val view = View(requireContext())
            val textview = TextView(requireContext())
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
                textview.typeface = requireContext().resources.getFont(callBacktv.typeface!!)
            }

            view.layoutParams = LinearLayout.LayoutParams(
                20,
                20
            )
            if (callBacktv.textColor == 0) {
                textview.setTextColor(requireContext().resources.getColor(R.color.black))
            }

            linearLayout.addView(view)
            linearLayout.addView(textview)


            textview.setOnLongClickListener {
                //title edit
                openPopUp("title", textview, "title", Constants.LINKFORHEADER)
                true
            }


        }
        if (callbackrv.size > 0) {

            bulletList = callbackrv
            val recycler = RecyclerView(requireContext())
            recycler.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            recycler.layoutManager = LinearLayoutManager(requireContext())
            recycler.adapter = BulletAdapter(requireContext(), callbackrv, type, fragmentName, this)
            linearLayout.addView(recycler)

        }

        if (callBacktv.data != "" || callbackrv.size > 0) {
            var isEdit: Boolean = false

            for (i in 0 until binding.texteditor.childCount) {
                val layout = binding.texteditor.getChildAt(i) as LinearLayout
                if (layout.tag.toString() == layoutid) {
                    isEdit = true
                    binding.texteditor.removeViewAt(i)
                }
            }
            var axixX = linearLayout.x
            var axixY = linearLayout.y
            if (layoutid == "") {
                linearLayout.tag = "layoutCount_$layoutcount"

            } else {
                linearLayout.tag = layoutid
                var lay = layoutid.split('_')
                idcount = lay[1].toInt()
                var laysplitcount = bulletArray.getJSONObject(idcount)
                axixX = laysplitcount.getString("axixX").toFloat()
                axixY = laysplitcount.getString("axixY").toFloat()
            }

            linearLayout.setOnClickListener(this)
            binding.texteditor.addView(linearLayout)
            val gson = Gson()


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

            if (!isEdit && layoutid == "") {

                layoutcount++
                this.bulletArray.put(bulletsJson)
                Log.d("data", this.bulletArray.toString())
            } else {
                this.bulletArray.put(idcount, bulletsJson)
                Log.d("data", this.bulletArray.toString())
            }


        }
        binding.text.requestFocus()
    }

    override fun popuponclick(data: String, itemView: View, list: ArrayList<Styles>, postion: Int) {
        //bullets Pop up
        bulletList = list
        openPopUp(data, itemView, "item", postion)
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
                        requireContext(), getString(R.string.Done), getString(R.string.cancel),
                        TYPE_BULLETS, this, fragmentName, styles, bulletList, layoutid
                    )
                checkBoxDiloag.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                checkBoxDiloag.show()
                binding.itemList.visibility = View.GONE
            }

            TYPE_CHECKBOX -> {
                val checkBoxDiloag =
                    BulletDialog(
                        requireContext(), getString(R.string.Done), getString(R.string.cancel),
                        TYPE_CHECKBOX, this, fragmentName, styles, bulletList, layoutid
                    )
                checkBoxDiloag.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                checkBoxDiloag.show()
                binding.itemList.visibility = View.GONE
            }

            TYPE_NUMBERLIST -> {
                val checkBoxDiloag =
                    BulletDialog(
                        requireContext(), getString(R.string.Done), getString(R.string.cancel),
                        TYPE_NUMBERLIST, this, fragmentName, styles, bulletList, layoutid
                    )
                checkBoxDiloag.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                checkBoxDiloag.show()
                binding.itemList.visibility = View.GONE
            }

            TYPE_ALPHALIST -> {
                val checkBoxDiloag =
                    BulletDialog(
                        requireContext(), getString(R.string.Done), getString(R.string.cancel),
                        TYPE_ALPHALIST, this, fragmentName, styles, bulletList, layoutid
                    )
                checkBoxDiloag.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                checkBoxDiloag.show()
                binding.itemList.visibility = View.GONE
            }

        }
    }

    private fun sendJson(action: String) {
        //   binding.loader.visibility = View.VISIBLE
        val layout = binding.texteditor
        val child = layout.childCount
        val editor = binding.photoEditorView
        val editorchildren = editor.childCount

        val dv: DrawingView = editor.getChildAt(2) as DrawingView
        var shape = dv.currentShape?.shape
        var dvv = dv.toString()


        val shapeData = dv.drawingPath.first


        gson = Gson()

        for (data in 0 until shapeData.size) {
            val brushData = gson.toJson(shapeData[data])
            brushArray.put(brushData)
        }

        var ii = 0
        var textarrayCount = 0
        var imageArrayCount = 0

        for (a in 3 until editorchildren) {

            val _data = editor.getChildAt(a) as FrameLayout
            val centreX = (_data.x).toString()
            val centreY = (_data.y).toString()
            val tag = _data.tag.toString()

            val height = _data.height.toString()
            val width = _data.width.toString()

            val gson = Gson()
//            _data.rotationX = 20f
//            _data.rotationY = 20f
            var _info: String? = ""
            if (listofTransform.size > ii) {

                _info = gson.toJson(listofTransform[ii])
            }
            if (tag.contains("TEXT")) {
                val textObject = textArray.getJSONObject(textarrayCount)
                textObject.put("axixX", centreX)
                textObject.put("axixY", centreY)
                textObject.put("width", width)
                textObject.put("height", height)
                textObject.put("info", _info)

                textArray.put(textarrayCount, textObject)
                textarrayCount += 1


            } else if (tag.contains("IMAGE")) {
                val imagejson = imageArray.getJSONObject(imageArrayCount)
                imagejson.put("axixX", centreX)
                imagejson.put("axixY", centreY)
                imagejson.put("width", width)
                imagejson.put("height", height)
                imagejson.put("info", _info)
                imageArray.put(imageArrayCount, imagejson)
                imageArrayCount += 1
            }
            ii++

        }
// SET
        for (i in 0 until bulletArray.length()) {

            var listoflinks = journalDatabase.getJournalData().getAllLinks()


            val _data = layout.getChildAt(i) as? LinearLayout


//            val centreX = (_data?.x!!).toString()
//            val centreY = (_data.y).toString()
            val jsonObject = bulletArray.getJSONObject(i)
            var id = jsonObject.get("layoutID")
            var bullets = jsonObject.get("bullet") as JSONArray
            val _index = bullets.length()

            val bulletslist: ArrayList<Styles> = ArrayList()
            for (j in 0 until _index) {
                val item = bullets.getString(j)
                val bullet: Styles = gson.fromJson(item, Styles::class.java)
                bulletslist.add(bullet)
            }
            var link =
                listoflinks.find { it.bulletnumber == Constants.LINKFORHEADER && it.layoutid == id }

            var gsonlink = gson.toJson(link)
            if (gsonlink == "null")
                gsonlink = ""

            // gson link gets empty when we choose LINKFORBULLET ,
//            jsonObject.put("axixX", centreX)
//            jsonObject.put("axixY", centreY)
            jsonObject.put("link", gsonlink)
            bulletArray.put(i, jsonObject)

            // Try to open bullets here and wrap into json
        }

        lifecycleScope.launch {
            journalDatabase.getJournalData().insertData(
                "1",
                bulletArray.toString(),
                textArray.toString(),
                imageArray.toString()
            )
        }

        if (action == "apiHit") {

            val indexdate = journalDatabase.getJournalData().getAllNotes()

            val data = indexdate[0]


            val pages = journalDatabase.getJournalData().getAllPages()

            val pageArray = JSONArray()
            for (i in 0 until pages.size) {
                val pagedetail = pages[i]
                val jsonPage = gson.toJson(pagedetail)
                pageArray.put(jsonPage)
            }


            val array: JSONObject = JSONObject()
            array.put("IndexBackground", data.indexBackground)
            array.put("ArrayofBullets", bulletArray)
            array.put("ArrayofText", textArray)
            array.put("ArrayofImage", imageArray)
            array.put("ArrayofStrokes", brushArray)
            array.put("Pages", pageArray)

            val arraystring = array.toString()

            saveJournal(arraystring, data)
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

        @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
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
        super.onMove(view, info)

        this.transinfo = info
        if (listofTransform.size == 0) {
            listofTransform.add(transinfo)
        } else {
            val contain = listofTransform.any {
                it.viewTag == transinfo.viewTag
            }
            if (contain) {
                for (i in 0 until listofTransform.size) {

                    if (listofTransform[i].viewTag == transinfo.viewTag) {
                        listofTransform.removeAt(i)
                        listofTransform.add(i, transinfo)
                        break

                    }
                }
            } else {
                listofTransform.add(transinfo)
            }
        }
    }

    private fun saveJournal(arraystring: String, data: JournalIndexTable) {

        // startIntersititialAd();

        binding.loader.visibility = View.VISIBLE
        var uri = Uri.parse("hello")
        if (data.coverImage.isNullOrEmpty()) {
            uri =
                Uri.parse("android.resource://" + requireActivity().packageName + "/" + R.drawable.whiteimage)
        } else {
            uri = Uri.parse(data.coverImage)
        }
        var listcheck: List<SelectedAudience> = ArrayList()
        val file = FileUtil.from(requireContext(), uri)
        if (data.privacy == "5") {
            listcheck = journalDatabase.getJournalData().getAllAudience()
        } else {

        }


        var getallpages = journalDatabase.getJournalData().getAllPages()

        var view = binding.linearLayout3
        var imageurl = Constants.takeScreenshot(view, requireActivity().applicationContext)



        if (data.jId == null) {
            val token = tinyDB.getString("token")
            jouralView.journalCreate(
                token!!,
                data.categoryId?.toInt()!!,
                data.journalTitle!!,
                data.coverColor!!,
                data.coverDescription!!,
                arraystring,
                data.privacy!!,
                "1",
                file,
                imageurl,
                listcheck,
                getallpages,
            )?.observe(requireActivity()) {

                when (it.status) {

                    Status.SUCCESS -> {
                        binding.loader.visibility = View.GONE
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }

                    Status.ERROR -> {
                        binding.loader.visibility = View.GONE
                    }

                    Status.LOADING -> {}

                }
            }
        } else {
            val token = tinyDB.getString("token")
            jouralView.journalUpdate(
                token!!,
                data.categoryId?.toInt()!!,
                data.journalTitle!!,
                data.coverColor!!,
                data.coverDescription!!,
                arraystring,
                data.privacy!!,
                "1",
                file,
                data.coverImageString,
                data.coverImage,
                imageurl,
                data.indexTemplateid!!,
                listcheck,
                data.jId,
                getallpages,
                data.numberofpages!! - 1,
            )?.observe(requireActivity()) {

                when (it.status) {

                    Status.SUCCESS -> {
                        binding.loader.visibility = View.GONE
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }

                    Status.ERROR -> {
                        binding.loader.visibility = View.GONE
                    }

                    Status.LOADING -> {}

                }
            }
        }


    }

    private fun openRoundBottomSheet(pages: Int) {
        val index = journalDatabase.getJournalData().getAllNotes()
        var indexbackground = index[0]
        bottomSheetDialog = RoundedBottomSheetDialog(requireContext())
        val bottomDialogView: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.bottom_dialog_pages, null)
        bottomSheetDialog.setContentView(bottomDialogView)

        val newPage: TextView
        val recyclerView: RecyclerView
        val indexlayout: ConstraintLayout

        indexlayout = bottomDialogView.findViewById(R.id.indexlayout)
        indexlayout.visibility = View.GONE
        newPage = bottomDialogView.findViewById<TextView>(R.id.newPage)
        recyclerView = bottomDialogView.findViewById(R.id.rv_pages)

        val list: ArrayList<Int> = ArrayList()
        for (i in 0 until pages) {
            list.add(i)
        }

        val _adapter = PagesAdapter(requireContext(), list, object : iOnClickListerner {
            override fun onclick(position: Int) {
                super.onclick(position)
                val bundle = Bundle()
                val pos = position.plus(1)
                bundle.putInt("pageid", pos)
                findNavController().navigate(
                    R.id.action_createJournalIndex_to_create_journal_entry,
                    bundle
                )

                bottomSheetDialog.dismiss()
            }
        })
        recyclerView.adapter = _adapter

        newPage.setOnClickListener(View.OnClickListener { view1: View? ->

            if (pages < 25) {
                val bundle = Bundle()
                val page = pages.plus(1)
                bundle.putInt("pageid", page)
                findNavController().navigate(
                    R.id.action_createJournalIndex_to_create_journal_entry,
                    bundle
                )
            } else {
                Toast.makeText(requireContext(), "Page limit exceeded", Toast.LENGTH_SHORT).show()
            }
            bottomSheetDialog.dismiss()
        })
        bottomSheetDialog.show()


    }

    private fun openDoodle() {
        bottomSheetDialog = RoundedBottomSheetDialog(requireContext())
        val bottomDialogView: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.bottom_dialog_pages, null)
        bottomSheetDialog.setContentView(bottomDialogView)

        val newPage: TextView

        val recyclerView: RecyclerView
        val tvtitle: TextView
        val noPage: TextView
        val indexlayout: ConstraintLayout
        val doodleStore: ImageView

        indexlayout = bottomDialogView.findViewById(R.id.indexlayout)
        noPage = bottomDialogView.findViewById(R.id.nopage)
        tvtitle = bottomDialogView.findViewById(R.id.tvTitle)
        newPage = bottomDialogView.findViewById<TextView>(R.id.newPage)
        recyclerView = bottomDialogView.findViewById(R.id.rv_pages)
        doodleStore = bottomDialogView.findViewById(R.id.doodlestore)
        doodleStore.visibility = View.VISIBLE

        doodleStore.setOnClickListener {
            val twoButtonDialog: TwoButtonDialog = TwoButtonDialog(
                requireContext(), "Discard Journal",
                "Are you sure, You want to discard this journal? and Continue to Doodle Store",
                getString(android.R.string.yes),
                getString(android.R.string.no),
                object : OnDialogClickListener {
                    override fun onDialogClick(callBack: String?) {
                        if (callBack == "Yes") {
                            requireActivity().finish()
                            startActivity(Intent(context, DoodleStore::class.java))
                        } else {
                        }
                    }
                })
            twoButtonDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            twoButtonDialog.show()
        }

        newPage.visibility = View.GONE
        tvtitle.text = "Doodle Pack"
        indexlayout.visibility = View.GONE
        val listDoodle = journalDatabase.getJournalData().getAllDoodles()

        if (listDoodle.size > 0) {
            val _adapter = DoodlePurchasedAdapter(
                requireContext(),
                listDoodle as ArrayList<PurchasedDoodles>,
                object : iOnClickListerner {
                    override fun onclick(position: Int) {
                        super.onclick(position)

                        val ran = (1..1000).random()
                        val viewtag = "IMAGE_$ran"
                        val json = JSONObject()
                        json.put("bitmap", listDoodle[position].doodle_image)
                        json.put("axixX", "0")
                        json.put("axixY", "0")
                        json.put("viewtag", viewtag)
                        imageArray.put(json)

                        Glide.with(requireContext())
                            .asBitmap()
                            .load(Constants.BASE_IMAGE + listDoodle[position].doodle_image)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    mPhotoEditor.addImage(
                                        resource,
                                        0f,
                                        0f,
                                        0f,
                                        0f, viewtag, object : onMoveListener {
                                            override fun onMove(view: View, info: TransformInfo) {
                                                super.onMove(view, info)
                                                transinfo = info

                                                if (listofTransform.size == 0) {
                                                    listofTransform.add(transinfo)
                                                } else {
                                                    var contain = listofTransform.any {
                                                        it.viewTag == transinfo.viewTag
                                                    }
                                                    if (contain) {
                                                        for (i in 0 until listofTransform.size) {

                                                            if (listofTransform[i].viewTag == transinfo.viewTag) {
                                                                listofTransform.removeAt(i)
                                                                listofTransform.add(i, transinfo)
                                                                break

                                                            }
                                                        }
                                                    } else {
                                                        listofTransform.add(transinfo)
                                                    }
                                                }


                                            }
                                        },
                                        true
                                    )
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                    // this is called when imageView is cleared on lifecycle call or for
                                    // some other reason.
                                    // if you are referencing the bitmap somewhere else too other than this imageView
                                    // clear it here as you can no longer have the bitmap
                                }
                            })


                        bottomSheetDialog.dismiss()
                    }
                })

            var manager = GridLayoutManager(requireContext(), 4)
            recyclerView.layoutManager = manager
            recyclerView.adapter = _adapter
        } else {
            noPage.text = "No Doodle Found"
            noPage.visibility = View.VISIBLE
        }
        bottomSheetDialog.show()


    }


}