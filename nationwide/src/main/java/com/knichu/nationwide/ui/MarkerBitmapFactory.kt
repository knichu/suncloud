package com.knichu.nationwide.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import com.knichu.common_ui.enums.WeatherIcon
import com.knichu.domain.constants.WeatherTempUnit
import com.knichu.domain.util.TemperatureParser
import com.naver.maps.map.overlay.OverlayImage

object MarkerBitmapFactory {

    fun create(context: Context, weatherCondition: String?, temperature: String?, unit: WeatherTempUnit = WeatherTempUnit.CELSIUS): OverlayImage =
        OverlayImage.fromBitmap(createBitmap(context, weatherCondition, TemperatureParser.convert(temperature, unit)))

    private fun createBitmap(context: Context, weatherCondition: String?, temperature: String?): Bitmap {
        val dm = context.resources.displayMetrics
        fun dp(v: Float) = (v * dm.density).toInt()
        fun sp(v: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, dm)

        val iconSize = dp(26f)
        val hPad = dp(8f)
        val vPad = dp(6f)
        val gap = dp(4f)
        val shadowRadius = dp(3f).toFloat()
        val shadowInset = dp(2f)

        val tempText = if (temperature != null) "${temperature}°" else "-"

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = sp(12f)
            typeface = Typeface.DEFAULT_BOLD
            color = Color.parseColor("#333333")
        }

        val textWidth = textPaint.measureText(tempText).toInt()
        val textHeight = (textPaint.descent() - textPaint.ascent()).toInt()
        val contentH = maxOf(iconSize, textHeight)
        val bitmapW = hPad + iconSize + gap + textWidth + hPad + shadowInset
        val bitmapH = vPad * 2 + contentH + shadowInset

        val bitmap = Bitmap.createBitmap(bitmapW, bitmapH, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val r = (bitmapH - shadowInset) / 2f
        val bgRect = RectF(
            shadowInset.toFloat(),
            shadowInset.toFloat(),
            (bitmapW - shadowInset).toFloat(),
            (bitmapH - shadowInset).toFloat()
        )

        // 그림자
        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#33000000")
            maskFilter = BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.NORMAL)
        }
        canvas.drawRoundRect(bgRect, r, r, shadowPaint)

        // 흰색 배경
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }
        canvas.drawRoundRect(bgRect, r, r, bgPaint)

        // 날씨 아이콘
        val iconTop = shadowInset + (bitmapH - shadowInset - iconSize) / 2
        val iconLeft = hPad + shadowInset
        AppCompatResources.getDrawable(context, WeatherIcon.getIconImage(weatherCondition).icon)
            ?.apply {
                setBounds(iconLeft, iconTop, iconLeft + iconSize, iconTop + iconSize)
                draw(canvas)
            }

        // 기온 텍스트
        val textX = (iconLeft + iconSize + gap).toFloat()
        val centerY = shadowInset + (bitmapH - shadowInset) / 2f
        val textY = centerY - (textPaint.ascent() + textPaint.descent()) / 2f
        canvas.drawText(tempText, textX, textY, textPaint)

        return bitmap
    }
}
