package com.potato.lrcview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class LrcView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    /*歌词数据*/
    private lateinit var mDatas : List<Lrc>
    /*歌词画笔*/
    private val mLrcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    /*当前正在播放歌词画笔*/
    private val mNowLrcPaint = Paint(Paint.ANTI_ALIAS_FLAG)


    init{
        mLrcPaint.color = Color.parseColor("#00ff00")
        mLrcPaint.textSize = dp2px(20f)

        mNowLrcPaint.color = Color.parseColor("#00ff00")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for(i in mDatas.indices){
            val item = mDatas.get(i)
            canvas?.drawText(item.content, 0F, 100F*i, mLrcPaint)
        }
    }
    /**
     * 设置歌词数据
     */
    public fun setLrcData(datas : List<Lrc>){
        if(datas.isEmpty())
            return
        this.mDatas = datas
        invalidate()
    }

    /**
     * dp转px
     */
    fun View.dp2px(dipValue: Float): Float {
        return (dipValue * this.resources.displayMetrics.density + 0.5f)
    }
}