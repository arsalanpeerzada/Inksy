package ja.burhanrashid52.photoeditor

import android.graphics.Typeface
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.inksy.R

/**
 * Created by Burhanuddin Rashid on 14/05/21.
 *
 * @author <https:></https:>//github.com/burhanrashid52>
 */
internal class Text(
    private val mPhotoEditorView: PhotoEditorView,
    private val mMultiTouchListener: MultiTouchListener,
    private val mViewState: PhotoEditorViewState,
    private val mDefaultTextTypeface: Typeface?,
    private val mGraphicManager: GraphicManager,
    private val isEnabled: Boolean
) : Graphic(
    context = mPhotoEditorView.context,
    graphicManager = mGraphicManager,
    viewType = ViewType.TEXT,
    layoutId = R.layout.view_photo_editor_text
) {
    private var mTextView: TextView? = null
    fun buildView(text: String?, styleBuilder: TextStyleBuilder?) {
        mTextView?.apply {
            this.text = text
            styleBuilder?.applyStyle(this)
        }
    }

    private fun setupGesture() {
        if (isEnabled) {
            val onGestureControl = buildGestureController(mPhotoEditorView, mViewState, isEnabled)
            mMultiTouchListener.setOnGestureControl(onGestureControl)
            val rootView = rootView
            rootView.setOnTouchListener(mMultiTouchListener)

            val centreX = (rootView.x).toString()
            val centreY = (rootView.y).toString()

            Log.d("Text_Coordinates", "$centreX : $centreY")
        }
    }

    override fun setupView(rootView: View) {
        mTextView = rootView.findViewById(R.id.tvPhotoEditorText)
        mTextView?.run {
            gravity = Gravity.CENTER
            typeface = mDefaultTextTypeface
        }
    }

    override fun updateView(view: View?) {
        val textInput = mTextView?.text.toString()
        val currentTextColor = mTextView?.currentTextColor ?: 0
        val photoEditorListener = mGraphicManager.onPhotoEditorListener
        photoEditorListener?.onEditTextChangeListener(view, textInput, currentTextColor)
    }

    init {
        setupGesture()
    }
}