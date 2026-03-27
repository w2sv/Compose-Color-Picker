package com.smarttoolfactory.colorpicker.model

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color
import com.smarttoolfactory.extendedcolors.util.RGBUtil

/**
 * Interface that can be polymorph into HSV, HSL or RGB color model
 */
sealed interface CompositeColor {
    val color: Color
    val argbHexString: String
    val rgbHexString: String
}

/**
 * Color in HSV color model
 */
data class ColorHSV(
    @FloatRange(from = 0.0, to = 360.0) val hue: Float,
    @FloatRange(from = 0.0, to = 1.0) val saturation: Float,
    @FloatRange(from = 0.0, to = 1.0) val value: Float,
    @FloatRange(from = 0.0, to = 1.0) val alpha: Float
) : CompositeColor {
    override val color: Color
        get() = Color.hsv(hue, saturation, value, alpha)

    override val argbHexString: String
        get() = RGBUtil.argbToHex(color.alpha, color.red, color.green, color.blue)

    override val rgbHexString: String
        get() = RGBUtil.rgbToHex(color.red, color.green, color.blue)
}

/**
 * Color in HSL color model
 */
data class ColorHSL(
    @FloatRange(from = 0.0, to = 360.0) val hue: Float,
    @FloatRange(from = 0.0, to = 1.0) val saturation: Float,
    @FloatRange(from = 0.0, to = 1.0) val lightness: Float,
    @FloatRange(from = 0.0, to = 1.0) val alpha: Float
) : CompositeColor {
    override val color: Color
        get() = Color.hsl(hue, saturation, lightness, alpha)

    override val argbHexString: String
        get() = RGBUtil.argbToHex(color.alpha, color.red, color.green, color.blue)

    override val rgbHexString: String
        get() = RGBUtil.rgbToHex(color.red, color.green, color.blue)
}

/**
 * Color in RGB color model
 */
data class ColorRGB(
    @FloatRange(from = 0.0, to = 1.0) val red: Float,
    @FloatRange(from = 0.0, to = 1.0) val green: Float,
    @FloatRange(from = 0.0, to = 1.0) val blue: Float,
    @FloatRange(from = 0.0, to = 1.0) val alpha: Float
) : CompositeColor {
    override val color: Color
        get() = Color(red, green, blue, alpha)

    override val argbHexString: String
        get() = RGBUtil.argbToHex(color.alpha, color.red, color.green, color.blue)

    override val rgbHexString: String
        get() = RGBUtil.rgbToHex(color.red, color.green, color.blue)
}
