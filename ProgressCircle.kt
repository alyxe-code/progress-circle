package com.p2lem8dev.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.p2lem8dev.learn_custom.R
import android.util.Log
import android.view.MotionEvent
import android.widget.RelativeLayout


/**
 * TODO: document your custom view class.
 */
class ProgressCircle : RelativeLayout {

    private var _progress: Float = 0f

    var progress: Float
        get() = _progress
        set(value) {
            _progress = value
            invalidate()
        }

    private var progressBarPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var _progressBarPrimaryColor: Int = Color.parseColor("#00AAFE")

    var primaryColor: Int
        get() = _progressBarPrimaryColor
        set(value) {
            _progressBarPrimaryColor = value
            invalidate()
        }

    private var _progressBarSecondColor: Int = Color.parseColor("#00EEFF")

    var secondaryColor: Int
        get() = _progressBarSecondColor
        set(value) {
            _progressBarSecondColor = value
            invalidate()
        }

    private val insideSpacePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var outlineColor: Int
        get() = outlinePaint.color
        set(value) {
            outlinePaint.color = value
            invalidate()
        }

    private var _radius: Float = width / 2f

    var radius: Float
        get() = _radius
        set(value) {
            _radius = value
            invalidate()
        }

    private var setRadiusByWidth: Boolean = false

    private var insideOffset: Float = 20f

    var progressBarWidth: Float
        get() = insideOffset
        set(value) {
            insideOffset = value
            invalidate()
        }

    private var _renderOutlineCircle: Boolean = true
    var isRenderOutlineCircle: Boolean
        get() = _renderOutlineCircle
        set(value) {
            _renderOutlineCircle = value
            invalidate()
        }

    private val oval = RectF()

    private var prevPosition = 0f
    private var progressMoveSpeed = .1f
    private var progressMoveFactor = .01f

    private var calcProgress = 0f

    private var _duration: Int = 0
    var duration
        get() = _duration
        set(value) {
            reset()
            _duration = value
        }

    val time: Int
        get() = Math.ceil((_progress / 100 * duration).toDouble()).toInt()

    private val tag = "_progress-touch"

    var progressChangeListener: OnProgressChangeListener? = null

    interface OnProgressChangeListener {
        fun onProgressChange(progress: Float, time: Int)
    }


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }


    private fun init(attrs: AttributeSet?, defStyle: Int) {
        setBackgroundColor(Color.TRANSPARENT)
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ProgressCircle, defStyle, 0
        )

        _progress = a.getInteger(R.styleable.ProgressCircle_android_progress, 0).toFloat()

        if (a.hasValue(R.styleable.ProgressCircle_radius)) {
            _radius = a.getDimension(R.styleable.ProgressCircle_radius, 300f)
            setRadiusByWidth = false
        } else {
            setRadiusByWidth = true
        }

        insideOffset = a.getDimension(R.styleable.ProgressCircle_progress_bar_size, 10f)

        insideSpacePaint.color = a.getColor(
            R.styleable.ProgressCircle_android_background,
            Color.parseColor("#F8F8F8")
        )
        _progressBarPrimaryColor = a.getColor(
            R.styleable.ProgressCircle_primary_color,
            Color.parseColor("#00AAFE")
        )
        _progressBarSecondColor = a.getColor(
            R.styleable.ProgressCircle_secondary_color,
            Color.parseColor("#00EEFF")
        )

        outlinePaint.color = a.getColor(
            R.styleable.ProgressCircle_outline_color,
            Color.TRANSPARENT
        )

        _renderOutlineCircle = a.getBoolean(R.styleable.ProgressCircle_outline, true)

        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val ovalWidth = radius
        val ovalHeight = radius
        val top = height / 2f - ovalHeight / 2
        val left = width / 2f - ovalWidth / 2
        oval.set(left, top, left + ovalWidth, top + ovalHeight)

        // outline timeline
        if (isRenderOutlineCircle) {
            canvas.drawCircle(
                width / 2f, height / 2f,
                radius / 2f, outlinePaint
            )
        }

        // _progress
        canvas.drawArc(
            oval, 180f, 360f / 100f * _progress,
            true, progressBarPaint
        )

        // inside
        canvas.drawCircle(
            width / 2f, height / 2f,
            radius / 2 - insideOffset, insideSpacePaint
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (setRadiusByWidth) {
            radius = width.toFloat()
        }
        progressBarPaint.shader = RadialGradient(
            width / 2.5f, height / 3f, radius,
            _progressBarPrimaryColor, _progressBarSecondColor, Shader.TileMode.MIRROR
        )
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        performClick()

        when (event.actionMasked) {
            MotionEvent.ACTION_UP -> {
                prevPosition = 0f
            }
            MotionEvent.ACTION_DOWN -> Log.d(tag, "down")
            MotionEvent.ACTION_MOVE -> handleMove(event)
        }

        return true
    }

    private fun handleMove(event: MotionEvent) {
        if (prevPosition == 0f) {
            prevPosition = event.x
            return
        }

        if (event.x == prevPosition) return
        calcProgress = if (event.x > prevPosition) {
            progressMoveSpeed + getMoveFactor(event.x)
        } else {
            (progressMoveSpeed + getMoveFactor(event.x)) * -1
        }

        val abs = Math.abs(calcProgress)

        // speed up according to acceleration
        if (abs in 0.6..0.8) calcProgress *= 5f
        if (abs in 0.8..1.0) calcProgress *= 8f
        if (abs >= 1f) calcProgress *= 10f

        if (abs > 20f) {
            calcProgress = 20f
        }

        _progress += calcProgress

        // fix _progress bar overflow
        if (_progress < 0) _progress = 0f
        if (_progress > 100) _progress = 100f

        progressChangeListener?.onProgressChange(_progress, time)

        invalidate()
        prevPosition = event.x
    }

    private fun getMoveFactor(x: Float): Float = Math.abs(x - prevPosition) * progressMoveFactor

    fun reset() {
        _progress = 0f
    }

    fun setCustomShader(shader: Shader) {
        progressBarPaint.shader = shader
    }

    fun translate(value: Int) {
        var v = value.toFloat()
        if (value > duration) v = duration.toFloat()
        if (value < 0) v = 0f
        val one = duration / 100f
        progress = v / one
        progressChangeListener?.onProgressChange(progress, time)
    }

}
