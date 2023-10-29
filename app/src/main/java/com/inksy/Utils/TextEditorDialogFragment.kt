package com.burhanrashid52.photoediting

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inksy.Interfaces.iOnClickListerner
import com.inksy.R
import com.inksy.UI.Adapter.FontAdapter
import com.inksy.Utils.ColorPickerAdapter

/**
 * Created by Burhanuddin Rashid on 1/16/2018.
 */
class TextEditorDialogFragment : DialogFragment() {
    private var mAddTextEditText: EditText? = null
    private var mAddTextDoneTextView: TextView? = null
    private var mInputMethodManager: InputMethodManager? = null
    var mColorCode = 0
    var selectedFont = 0
    private var mTextEditor: TextEditor? = null
    private lateinit var font_button : ImageView
    private lateinit var color_pallete : ImageView
    lateinit var addTextColorPickerRecyclerView: RecyclerView
    lateinit var addFontPickerRecyclerView : RecyclerView
    lateinit var bold : ImageView
    lateinit var italic : ImageView
    var isBold: Boolean = false
    var isItalic: Boolean = false
    lateinit var plus : ImageView


    interface TextEditor {
        fun onDone(inputText: String?, colorCode: Int, typface : Int, isBold : Boolean , isItalic : Boolean)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        //Make dialog full screen with transparent background
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_text_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAddTextEditText = view.findViewById(R.id.add_text_edit_text)
        mInputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mAddTextDoneTextView = view.findViewById(R.id.add_text_done_tv)
        mAddTextEditText?.requestFocus()
        addTextColorPickerRecyclerView = view.findViewById(R.id.add_text_color_picker_recycler_view)
        addFontPickerRecyclerView = view.findViewById(R.id.add_font_picker_recycler_view)
        font_button = view.findViewById(R.id.fonts)
        color_pallete = view.findViewById(R.id.colorplatter)
        //selectedFont = R.font.sfmedium
        bold = view.findViewById(R.id.bold)
        italic = view.findViewById(R.id.italic)
        plus = view.findViewById(R.id.plus)


        //Setup the color picker for text color

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        addTextColorPickerRecyclerView.layoutManager = layoutManager
        addTextColorPickerRecyclerView.setHasFixedSize(true)
        val colorPickerAdapter = ColorPickerAdapter(requireActivity())
        //This listener will change the text color when clicked on any color from picker
        colorPickerAdapter.setOnColorPickerClickListener(object :
            ColorPickerAdapter.OnColorPickerClickListener {
            override fun onColorPickerClickListener(colorCode: Int) {
                mColorCode = colorCode
                mAddTextEditText!!.setTextColor(colorCode)
            }
        })


        addTextColorPickerRecyclerView.adapter = colorPickerAdapter
        addFontPickerRecyclerView.setHasFixedSize(true)
        addFontPickerRecyclerView.adapter = FontAdapter(activity?.baseContext!!,object : iOnClickListerner{
            override fun onclick(typeface: Int) {
                selectedFont = typeface
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mAddTextEditText?.typeface = context?.resources?.getFont(selectedFont)
                }
            }
        })
        mAddTextEditText!!.setText(requireArguments().getString(EXTRA_INPUT_TEXT))
        mColorCode = requireArguments().getInt(EXTRA_COLOR_CODE)
        mAddTextEditText!!.setTextColor(mColorCode)
       // mAddTextEditText!!.setTypeface()
        mInputMethodManager!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        //Make a callback on activity when user is done with text editing
        mAddTextDoneTextView!!.setOnClickListener {
            mInputMethodManager!!.hideSoftInputFromWindow(it.windowToken, 0)
            dismiss()
            val inputText = mAddTextEditText!!.text.toString()
            if (!TextUtils.isEmpty(inputText) && mTextEditor != null) {
                mTextEditor!!.onDone(inputText, mColorCode,selectedFont,isBold,isItalic)
            }
        }

        font_button.setOnClickListener {
            if (addFontPickerRecyclerView.visibility == View.VISIBLE){
                addFontPickerRecyclerView.visibility = View.GONE
            }else {
                addFontPickerRecyclerView.visibility = View.VISIBLE
            }

        }

        color_pallete.setOnClickListener {
            if (addTextColorPickerRecyclerView.visibility == View.VISIBLE){
                addTextColorPickerRecyclerView.visibility = View.GONE
            }else {
                addTextColorPickerRecyclerView.visibility = View.VISIBLE
            }
        }

        bold.setOnClickListener {
            if (!isBold) {
                isBold = true
                selectUnselect(bold, isBold)
            } else {
                isBold = false
                selectUnselect(bold, isBold)
            }

        }
        italic.setOnClickListener {
            if (!isItalic) {
                isItalic = true
                selectUnselect(italic, isItalic)
            } else {
                isItalic = false
                selectUnselect(italic, isItalic)
            }
        }
        plus.setOnClickListener {
            openPopUp(){}
        }

    }

    private fun openPopUp(function: () -> Unit) {
        val contextWrapper = ContextThemeWrapper( requireContext(), R.style.popupMenuStyle)
        val popupMenu = PopupMenu(
            contextWrapper, plus
        )
        popupMenu.setForceShowIcon(true)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
//                R.id.link -> {
//
////                    var navController: NavController? = null
////                    val action =
////                        CreateJournalIndexDirections.actionCreateJournalIndexToCreateJournalEntry()
////                    navController = Navigation.findNavController(plus)
////
////                    navController.navigate(action)
//                    return@OnMenuItemClickListener true
//                }
                R.id.Edit -> {
                    Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show()
                    return@OnMenuItemClickListener true
                }
                else -> false
            }


        })

        popupMenu.inflate(R.menu.pop_up)
        popupMenu.show()
        popupMenu.menu.findItem(R.id.link_bullet).isVisible = false
        popupMenu.menu.findItem(R.id.link_title).isVisible = false
        popupMenu.menu.findItem(R.id.Edit).isVisible = false
    }

    //Callback to listener if user is done with text editing
    fun setOnTextEditorListener(textEditor: TextEditor?) {
        mTextEditor = textEditor
    }

    companion object {
        private val TAG: String = TextEditorDialogFragment::class.java.simpleName
        const val EXTRA_INPUT_TEXT = "extra_input_text"
        const val EXTRA_COLOR_CODE = "extra_color_code"
        const val EXTRA_TEXT_TYPEFACE = "extra_text_typeface"

        //Show dialog with provide text and text color
        //Show dialog with default text input as empty and text color white
        @JvmStatic
        @JvmOverloads
        fun show(appCompatActivity: AppCompatActivity,
                 inputText: String =
                     "",
                 @ColorInt colorCode: Int = ContextCompat.getColor(appCompatActivity, R.color.white)): TextEditorDialogFragment {
            val args = Bundle()
            args.putString(EXTRA_INPUT_TEXT, inputText)
            args.putInt(EXTRA_COLOR_CODE, colorCode)

            val fragment = TextEditorDialogFragment()
            fragment.arguments = args
            fragment.show(appCompatActivity.supportFragmentManager, TAG)
            return fragment
        }
    }

    private fun selectUnselect(imageview: ImageView, selected: Boolean) {

        if (selected) {
            imageview.setBackgroundColor(context?.resources?.getColor(R.color.black)!!)
            imageview.setColorFilter(Color.argb(255, 255, 255, 255))
        } else {
            imageview.setBackgroundColor(context?.resources?.getColor(R.color.line_grey_trans)!!)
            imageview.setColorFilter(Color.argb(0, 0, 0, 0))
        }
    }
}