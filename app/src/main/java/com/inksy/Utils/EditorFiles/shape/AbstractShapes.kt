package ja.burhanrashid52.photoeditor.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF

class AbstractShapes(protected val tag: String) : Shape {
    var path = Path()
    var left = 0f
    var top = 0f
    var right = 0f
    var bottom = 0f

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawPath(path, paint)
    }

    override fun startShape(x: Float, y: Float) {

    }

    override fun moveShape(x: Float, y: Float) {

    }

    override fun stopShape() {
    }

    private val bounds: RectF
        get() {
            val bounds = RectF()
            path.computeBounds(bounds, true)
            return bounds
        }

    fun hasBeenTapped(): Boolean {
        val bounds = bounds
        return bounds.top < TOUCH_TOLERANCE && bounds.bottom < TOUCH_TOLERANCE && bounds.left < TOUCH_TOLERANCE && bounds.right < TOUCH_TOLERANCE
    }

    override fun toString(): String {
        return tag +
                ": left: " + left +
                " - top: " + top +
                " - right: " + right +
                " - bottom: " + bottom
    }

    companion object {
        const val TOUCH_TOLERANCE = 4f
    }
}