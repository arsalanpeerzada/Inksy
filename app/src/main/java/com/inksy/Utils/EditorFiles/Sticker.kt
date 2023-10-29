package ja.burhanrashid52.photoeditor

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.inksy.R

/**
 * Created by Burhanuddin Rashid on 14/05/21.
 *
 * @author <https:></https:>//github.com/burhanrashid52>
 */
internal class Sticker(
    private val mPhotoEditorView: PhotoEditorView,
    private val mMultiTouchListener: MultiTouchListener,
    private val mViewState: PhotoEditorViewState,
    graphicManager: GraphicManager?,
    private val isEnabled: Boolean
) : Graphic(
    context = mPhotoEditorView.context,
    graphicManager = graphicManager,
    viewType = ViewType.IMAGE,
    layoutId = R.layout.view_photo_editor_image
) {
    private var imageView: ImageView? = null
    fun buildView(desiredImage: Bitmap?) {
        imageView?.setImageBitmap(desiredImage)
    }

    private fun setupGesture() {
        if (isEnabled) {
            val onGestureControl = buildGestureController(mPhotoEditorView, mViewState, isEnabled)
            mMultiTouchListener.setOnGestureControl(onGestureControl)
            val rootView = rootView
            rootView.setOnTouchListener(mMultiTouchListener)
        } else {
            val frmBorder = rootView.findViewById<View>(R.id.frmBorder)
            val imgClose = rootView.findViewById<View>(R.id.imgPhotoEditorClose)

            frmBorder?.setBackgroundResource(0)
            imgClose.visibility = View.GONE
        }
    }

    override fun setupView(rootView: View) {
        imageView = rootView.findViewById(R.id.imgPhotoEditorImage)
    }

    init {
        setupGesture()
    }
}