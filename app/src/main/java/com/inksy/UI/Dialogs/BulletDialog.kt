package com.inksy.UI.Dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence
import com.inksy.Interfaces.OnDialogBulletClickListener
import com.inksy.Interfaces.PopUpOnClickListerner
import com.inksy.Interfaces.iLinkOnClickListener
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.Model.Styles
import com.inksy.R
import com.inksy.UI.Adapter.BulletAdapter
import com.inksy.UI.Adapter.FontAdapter
import com.inksy.Utils.ColorPickerAdapter

class BulletDialog(
    context: Context,
    private var positive: String,
    private var negative: String,
    var type: Int,
    var listener: OnDialogBulletClickListener,
    var fragment: String,
    var styles: Styles,
    var bulletList: ArrayList<Styles>,
    var layoutid: String
) : Dialog(context), iLinkOnClickListener, iOnClickListerner, PopUpOnClickListerner {

    lateinit var tvCancel: TextView
    lateinit var tvConfirm: TextView
    lateinit var tvTitle: TextView
    lateinit var tvDescription: TextView
    lateinit var addbutton: ImageView
    lateinit var rv_bullets: RecyclerView
    private var bullet_list: ArrayList<Styles> = ArrayList()
    lateinit var adapter: BulletAdapter
    lateinit var titleText: EditText
    lateinit var bulletText: EditText
    lateinit var colorPickRV: RecyclerView
    lateinit var fontPickRV: RecyclerView
    var count = 0
    lateinit var bold: ImageView
    lateinit var italic: ImageView
    lateinit var underline: ImageView
    lateinit var strikethrough: ImageView
    lateinit var colorpaletter: ImageView
    lateinit var font: ImageView
    var colorcode: Int = 0
    var isBold: Boolean = false
    var isItalic: Boolean = false
    var isUnderLine: Boolean = false
    var isStrikeThrough: Boolean = false

    var colorcode_title: Int = 0
    var isBold_title: Boolean = false
    var isItalic_title: Boolean = false
    var isUnderLine_title: Boolean = false
    var isStrikeThrough_title: Boolean = false
    var typeface_title: Int = 0
    var fontsize_title: Int = 18
    var title_selected = false
    var typeface: Int = 0
    lateinit var fontSizeUp: ImageView
    lateinit var fontSizeDown: ImageView
    lateinit var fontView: TextView
    var fontsize: Int = 14
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        colorcode = ContextCompat.getColor(context, R.color.black)

        setContentView(R.layout.create_bullets_dialog)
        setCanceledOnTouchOutside(true)
        val window = window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val fontAdapter = FontAdapter(context = context, iclick = this)
        adapter = BulletAdapter(context, bullet_list, type, fragment, this)

        uiSetup()
        uiListener()
        checkData()

        val colorPickerAdapter = ColorPickerAdapter(context)
        //This listener will change the text color when clicked on any color from picker
        colorPickerAdapter.setOnColorPickerClickListener(object :
            ColorPickerAdapter.OnColorPickerClickListener {
            override fun onColorPickerClickListener(colorCodee: Int) {

                if (title_selected) {
                    colorcode_title = colorCodee
                    titleText.setTextColor(colorcode_title)
                } else {
                    colorcode = colorCodee
                    bulletText.setTextColor(colorcode)

                }

            }
        })




        colorPickRV.adapter = colorPickerAdapter

        fontPickRV.adapter = fontAdapter


        fontAdapter.reset()

        adapter = BulletAdapter(context, list = bullet_list, type, fragment, this)
        rv_bullets.adapter = adapter
        tvConfirm.text = positive
        tvCancel.text = negative


        titleText.setOnFocusChangeListener { v, hasFocus ->
            title_selected = hasFocus
            Log.d("focusCheck", title_selected.toString())

            if (title_selected) {
                selectUnselect(bold, isBold_title)
                selectUnselect(underline, isUnderLine_title)
                selectUnselect(italic, isItalic_title)
                selectUnselect(strikethrough, isStrikeThrough_title)
                fontView.text = fontsize_title.toString()


            } else {
                selectUnselect(bold, false)
                selectUnselect(underline, false)
                selectUnselect(italic, false)
                selectUnselect(strikethrough, false)
                fontView.text = fontsize.toString()
            }
        }

    }

    private fun checkData() {
        if (!styles.data.isNullOrEmpty()) {

            titleText.setText(styles.data!!)
            typeface_title = styles.typeface!!
            colorcode_title = styles.textColor!!
            isBold_title = styles.isBold!!
            isItalic_title = styles.isItalic!!
            isUnderLine_title = styles.isunderline!!
            isStrikeThrough_title = styles.isstrike!!
            fontsize_title = styles.fontsize!!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                titleText.typeface = context.resources.getFont(styles.typeface!!)
            }
            titleText.textSize = styles.fontsize!!.toFloat()
            titleText.setTextColor(colorcode_title)
        }
        if (bulletList.size > 0) {
            bullet_list = bulletList
            adapter.notifyDataSetChanged()
            count = bulletList.size
        }
    }

    private fun uiListener() {
        tvCancel.setOnClickListener {
            dismiss()
        }
        tvConfirm.setOnClickListener {


            if (bullet_list.size > 0) {

                styles = Styles(
                    titleText.text.toString(),
                    isBold_title,
                    isItalic_title,
                    isUnderLine_title,
                    isStrikeThrough_title,
                    colorcode_title,
                    typeface_title,
                    fontsize_title
                )

                listener.onDialogClick(
                    styles, bullet_list, type, layoutid
                )
                dismiss()
            } else {
                Toast.makeText(context, context.getString(R.string.list_error), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        bold.setOnClickListener {

            if (title_selected) {
                if (!isBold_title) {
                    isBold_title = true
                    selectUnselect(bold, isBold_title)
                } else {
                    isBold_title = false
                    selectUnselect(bold, isBold_title)
                }
            } else {
                if (!isBold) {
                    isBold = true
                    selectUnselect(bold, isBold)
                } else {
                    isBold = false
                    selectUnselect(bold, isBold)
                }
            }


        }
        italic.setOnClickListener {

            if (title_selected) {
                if (!isItalic_title) {
                    isItalic_title = true
                    selectUnselect(italic, isItalic_title)
                } else {
                    isItalic_title = false
                    selectUnselect(italic, isItalic_title)
                }
            } else {
                if (!isItalic) {
                    isItalic = true
                    selectUnselect(italic, isItalic)
                } else {
                    isItalic = false
                    selectUnselect(italic, isItalic)
                }
            }

        }
        underline.setOnClickListener {

            if (title_selected) {
                if (!isUnderLine_title) {
                    isUnderLine_title = true
                    selectUnselect(underline, isUnderLine_title)
                } else {
                    isUnderLine_title = false
                    selectUnselect(underline, isUnderLine_title)
                }
            } else {
                if (!isUnderLine) {
                    isUnderLine = true
                    selectUnselect(underline, isUnderLine)
                } else {
                    isUnderLine = false
                    selectUnselect(underline, isUnderLine)
                }
            }


        }
        strikethrough.setOnClickListener {

            if (title_selected) {
                if (!isStrikeThrough_title) {
                    isStrikeThrough_title = true
                    selectUnselect(strikethrough, isStrikeThrough_title)
                } else {
                    isStrikeThrough_title = false
                    selectUnselect(strikethrough, isStrikeThrough_title)
                }
            } else {
                if (!isStrikeThrough) {
                    isStrikeThrough = true
                    selectUnselect(strikethrough, isStrikeThrough)

                } else {
                    isStrikeThrough = false
                    selectUnselect(strikethrough, isStrikeThrough)
                }
            }


        }
        colorpaletter.setOnClickListener {
            if (colorPickRV.isVisible) {

                colorPickRV.visibility = View.GONE
            } else {
                colorPickRV.visibility = View.VISIBLE
            }
        }

        font.setOnClickListener {
            if (fontPickRV.isVisible) {

                fontPickRV.visibility = View.GONE
            } else {
                fontPickRV.visibility = View.VISIBLE
            }
        }

        addbutton.setOnClickListener {
            if (bulletText.text.toString().isEmpty()) {
                Toast.makeText(context, "Text Field is empty", Toast.LENGTH_SHORT).show()
            } else {
                val data = bulletText.text.toString()

                val bulletstyle =
                    Styles(
                        data,
                        isBold,
                        isItalic,
                        isUnderLine,
                        isStrikeThrough,
                        colorcode,
                        typeface,
                        fontsize
                    )


                bullet_list.add(bulletstyle)
                adapter.notifyItemChanged(count, bulletText)
                rv_bullets.scrollToPosition(bullet_list.size - 1)
                count++
                bulletText.text.clear()
                isBold = false
                isUnderLine = false
                isItalic = false
                isStrikeThrough = false


                selectUnselect(bold, isBold)
                selectUnselect(underline, isUnderLine)
                selectUnselect(italic, isItalic)
                selectUnselect(strikethrough, isStrikeThrough)


            }
        }

        fontSizeUp.setOnClickListener {

            if (title_selected) {
                fontsize_title += 2
                fontView.text = fontsize_title.toString()
            } else {
                fontsize += 2
                fontView.text = fontsize.toString()
            }
        }
        fontSizeDown.setOnClickListener {
            if (title_selected) {
                fontsize_title -= 2
                fontView.text = fontsize_title.toString()
            } else {
                fontsize -= 2
                fontView.text = fontsize.toString()
            }
        }


    }


    private fun uiSetup() {
        tvTitle = findViewById(R.id.tvTitle)
        tvDescription = findViewById(R.id.tvDescription)
        tvCancel = findViewById(R.id.tvCancel)
        tvConfirm = findViewById(R.id.tvConfirm)
        addbutton = findViewById(R.id.add)
        rv_bullets = findViewById(R.id.rv_bullets)
        titleText = findViewById(R.id.TitleText)
        bulletText = findViewById(R.id.BulletText)
        colorPickRV = findViewById(R.id.colorpicker)
        bold = findViewById(R.id.bold)
        italic = findViewById(R.id.italic)
        underline = findViewById(R.id.underline)
        strikethrough = findViewById(R.id.cut)
        colorpaletter = findViewById(R.id.colorplatter)
        font = findViewById(R.id.fonts)
        fontPickRV = findViewById(R.id.fontPicker)
        typeface = R.font.sfmedium
        typeface_title = R.font.sfmedium
        fontSizeUp = findViewById(R.id.fontsizeup)
        fontSizeDown = findViewById(R.id.fontsizedown)
        fontView = findViewById(R.id.fontsize)
        var toolbox = findViewById<View>(R.id.view19)


//        var activity = context as Activity
//        var headerShowcase = BubbleShowCaseBuilder(activity) //Activity instance
//            .title("Header")
//            .description("Here you can right the header")//Any title for the bubble view
//            .backgroundColor(ContextCompat.getColor(activity, R.color.white))
//            .textColor(ContextCompat.getColor(activity, R.color.black))
//            .targetView(titleText) //View to point out
//
//        var bulletTextShowcase = BubbleShowCaseBuilder(activity) //Activity instance
//            .title("Header")
//            .description("Here you can right the header")//Any title for the bubble view
//            .backgroundColor(ContextCompat.getColor(activity, R.color.white))
//            .textColor(ContextCompat.getColor(activity, R.color.black))
//            .targetView(bulletText) //View to point out
//
//        var addShowcase = BubbleShowCaseBuilder(activity) //Activity instance
//            .title("Header")
//            .description("Here you can add bullet to the list")//Any title for the bubble view
//            .backgroundColor(ContextCompat.getColor(activity, R.color.white))
//            .textColor(ContextCompat.getColor(activity, R.color.black))
//            .targetView(addbutton) //View to point out
//
////        Here you can make bullet text, bold, italic, underline and cross, increase and decrease size of the text and change the font as well
//
//
//        var boldShowcase = BubbleShowCaseBuilder(activity) //Activity instance
//            .title("Header")
//            .description("Here you can make bullet text bold")//Any title for the bubble view
//            .backgroundColor(ContextCompat.getColor(activity, R.color.white))
//            .textColor(ContextCompat.getColor(activity, R.color.black))
//            .targetView(bold) //View to point out
//
//        var italicShowcase = BubbleShowCaseBuilder(activity) //Activity instance
//            .title("Header")
//            .description("Here you can make bullet text italic")//Any title for the bubble view
//            .backgroundColor(ContextCompat.getColor(activity, R.color.white))
//            .textColor(ContextCompat.getColor(activity, R.color.black))
//            .targetView(italic) //View to point out
//
//        var underlineShowcase = BubbleShowCaseBuilder(activity) //Activity instance
//            .title("Header")
//            .description("Here you can make bullet text underline")//Any title for the bubble view
//            .backgroundColor(ContextCompat.getColor(activity, R.color.white))
//            .textColor(ContextCompat.getColor(activity, R.color.black))
//            .targetView(underline) //View to point out
//
//        var upShowcase = BubbleShowCaseBuilder(activity) //Activity instance
//            .title("Header")
//            .description("Here you can make bullet text size increase")//Any title for the bubble view
//            .backgroundColor(ContextCompat.getColor(activity, R.color.white))
//            .textColor(ContextCompat.getColor(activity, R.color.black))
//            .targetView(strikethrough) //View to point out
//
//
//        var downShowcase = BubbleShowCaseBuilder(activity) //Activity instance
//            .title("Header")
//            .description("Here you can make bullet text size decrease")//Any title for the bubble view
//            .backgroundColor(ContextCompat.getColor(activity, R.color.white))
//            .textColor(ContextCompat.getColor(activity, R.color.black))
//            .targetView(strikethrough) //View to point out


//        BubbleShowCaseSequence()
//            .addShowCase(headerShowcase)
//            .addShowCase(bulletTextShowcase)
//            .addShowCase(addShowcase)
//            .show()


    }

    override fun linkOnClick(position: Int, view: View) {

    }

    private fun selectUnselect(imageview: ImageView, selected: Boolean) {

        if (selected) {
            imageview.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            imageview.setColorFilter(Color.argb(255, 255, 255, 255))
        } else {
            imageview.setBackgroundColor(ContextCompat.getColor(context, R.color.line_grey_trans))
            imageview.setColorFilter(Color.argb(0, 0, 0, 0))
        }
    }


    override fun onclick(_typeface: Int) {

        if (title_selected) {
            typeface_title = _typeface
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                titleText.typeface = context.resources.getFont(typeface_title)
            }
        } else {
            typeface = _typeface
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                bulletText.typeface = context.resources.getFont(typeface)
            }
        }

    }

    override fun popuponclick(
        data: String,
        itemView: View,
        list: ArrayList<Styles>,
        position: Int
    ) {

    }

}