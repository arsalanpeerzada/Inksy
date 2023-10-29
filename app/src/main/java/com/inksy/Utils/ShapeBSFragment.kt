package com.burhanrashid52.photoediting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inksy.R
import com.inksy.Utils.ColorPickerAdapter
import ja.burhanrashid52.photoeditor.shape.ShapeType

class ShapeBSFragment : BottomSheetDialogFragment(), OnSeekBarChangeListener {
    private var mProperties: Properties? = null
    lateinit var rvColor: RecyclerView
    lateinit var sbOpacity: SeekBar
    lateinit var sbBrushSize: SeekBar
    lateinit var shapeGroup: RadioGroup

    interface Properties {
        fun onColorChanged(colorCode: Int)
        fun onOpacityChanged(opacity: Int)
        fun onShapeSizeChanged(shapeSize: Int)
        fun onShapePicked(shapeType: ShapeType?)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_shapes_dialog, container, false)
    }


    fun hidecolorList() {
        rvColor.visibility = View.GONE
    }

    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
        when (seekBar.id) {
            R.id.shapeOpacity -> if (mProperties != null) {
                mProperties!!.onOpacityChanged(i)
            }
            R.id.shapeSize -> if (mProperties != null) {
                mProperties!!.onShapeSizeChanged(i)
            }
        }
    }

    fun getColorList() {

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvColor.layoutManager = layoutManager
        rvColor.setHasFixedSize(true)
        val colorPickerAdapter = ColorPickerAdapter(requireActivity())
        colorPickerAdapter.setOnColorPickerClickListener(object :
            ColorPickerAdapter.OnColorPickerClickListener {
            override fun onColorPickerClickListener(colorCode: Int) {
                if (mProperties != null) {
                    dismiss()
                    mProperties!!.onColorChanged(colorCode)
                }
            }
        })
        rvColor.adapter = colorPickerAdapter

        rvColor.visibility = View.VISIBLE
    }

    fun setPropertiesChangeListener(properties: Properties?) {
        mProperties = properties
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvColor = view.findViewById(R.id.shapeColors)

        sbOpacity = view.findViewById<SeekBar>(R.id.shapeOpacity)

        sbBrushSize = view.findViewById<SeekBar>(R.id.shapeSize)

        shapeGroup = view.findViewById<RadioGroup>(R.id.shapeRadioGroup)

        // shape picker
        shapeGroup.setOnCheckedChangeListener { _: RadioGroup?, checkedId: Int ->

            when (checkedId) {
                R.id.lineRadioButton -> {
                    mProperties!!.onShapePicked(ShapeType.LINE)
                    getColorList()
                }
                R.id.ovalRadioButton -> {
                    mProperties!!.onShapePicked(ShapeType.OVAL)
                    getColorList()
                }
                R.id.rectRadioButton -> {
                    mProperties!!.onShapePicked(ShapeType.RECTANGLE)
                    getColorList()
                }
                R.id.Eraser -> {
                    mProperties!!.onColorChanged(1)
                    hidecolorList()
                }
                else -> {
                    getColorList()
                    mProperties!!.onShapePicked(ShapeType.BRUSH)
                }
            }
        }
        sbOpacity.setOnSeekBarChangeListener(this)
        sbBrushSize.setOnSeekBarChangeListener(this)
        getColorList()

    }



    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}