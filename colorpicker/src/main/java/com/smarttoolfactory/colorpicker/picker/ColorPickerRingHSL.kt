package com.smarttoolfactory.colorpicker.picker

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.colorpicker.model.ColorHSL
import com.smarttoolfactory.colorpicker.model.ColorModel
import com.smarttoolfactory.colorpicker.selector.SelectorDiamondSaturationLightnessHSL
import com.smarttoolfactory.colorpicker.selector.SelectorRectSaturationLightnessHSL
import com.smarttoolfactory.colorpicker.selector.SelectorRingHue
import com.smarttoolfactory.colorpicker.slider.CompositeSliderPanel
import com.smarttoolfactory.colorpicker.widget.ColorModelChangeTabRow
import com.smarttoolfactory.extendedcolors.util.ColorUtil.colorToHSL

@Preview
@Composable
private fun Prev() {
    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyLarge) {
        ColorPickerRingHSL(
            initialColor = Color.Red,
            colorModel = ColorModel.RGB,
            ringOuterRadiusFraction = .85f,
            showAlphaSlider = false,
            isColorModelSelectable = false,
            onColorChange = {}
        )
    }
}

/**
 * ColorPicker with [SelectorRingHue] hue selector and [SelectorRectSaturationLightnessHSL]
 * saturation lightness Selector that uses [HSL](https://en.wikipedia.org/wiki/HSL_and_HSV)
 * color model as base.
 * This color picker has tabs section that can be changed between
 * HSL, HSV and RGB color models and color can be set using [CompositeSliderPanel] which contains
 * sliders for each color models.
 *
 * @param initialColor color that is passed to this picker initially.
 * @param ringOuterRadiusFraction outer radius of [SelectorRingHue].
 * @param ringInnerRadiusFraction inner radius of [SelectorRingHue].
 * @param ringBackgroundColor background from center to inner radius of [SelectorRingHue].
 * @param ringBorderStrokeColor stroke color for drawing borders around inner or outer radius.
 * @param ringBorderStrokeWidth stroke width of borders.
 * @param selectionRadius radius of white and black circle selector.
 * @param onColorChange callback that is triggered when [Color] is changed using [SelectorRingHue],
 * [SelectorDiamondSaturationLightnessHSL] or [CompositeSliderPanel]
 */
@Composable
fun ColorPickerRingHSL(
    initialColor: Color,
    colorModel: ColorModel,
    modifier: Modifier = Modifier,
    saturationLightnessSelectorShape: SaturationLightnessSelectorShape = SaturationLightnessSelectorShape.Rect,
    ringOuterRadiusFraction: Float = .9f,
    ringInnerRadiusFraction: Float = .6f,
    ringBackgroundColor: Color = Color.Transparent,
    ringBorderStrokeColor: Color = Color.Black,
    ringBorderStrokeWidth: Dp = 1.dp,
    selectionRadius: Dp = 8.dp,
    showAlphaSlider: Boolean = true,
    isColorModelSelectable: Boolean = true,
    onColorChange: (Color) -> Unit
) {
    var inputColorModel by remember(colorModel) { mutableStateOf(colorModel) }

    val hslArray = colorToHSL(initialColor)

    var hue by remember { mutableFloatStateOf(hslArray[0]) }
    var saturation by remember { mutableFloatStateOf(hslArray[1]) }
    var lightness by remember { mutableFloatStateOf(hslArray[2]) }
    var alpha by remember { mutableFloatStateOf(initialColor.alpha) }

    val currentColor = remember(hue, saturation, lightness, alpha) {
        Color.hsl(
            hue = hue,
            saturation = saturation,
            lightness = lightness,
            alpha = alpha
        )
    }

    LaunchedEffect(hue, saturation, lightness, alpha) { onColorChange(currentColor) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Ring Shaped Hue Selector
            SelectorRingHue(
                modifier = Modifier.fillMaxWidth(1f),
                hue = hue,
                outerRadiusFraction = ringOuterRadiusFraction,
                innerRadiusFraction = ringInnerRadiusFraction,
                backgroundColor = ringBackgroundColor,
                borderStrokeColor = ringBorderStrokeColor,
                borderStrokeWidth = ringBorderStrokeWidth,
                selectionRadius = selectionRadius,
                onChange = { hue = it }
            )

            // Saturation Lightness Selector
            AnimatedContent(saturationLightnessSelectorShape) { shape ->
                when (shape) {
                    SaturationLightnessSelectorShape.Rect ->
                        SelectorRectSaturationLightnessHSL(
                            modifier = Modifier
                                .fillMaxWidth(ringInnerRadiusFraction * .65f)
                                .aspectRatio(1f),
                            hue = hue,
                            saturation = saturation,
                            lightness = lightness,
                            selectionRadius = selectionRadius
                        ) { s, l ->
                            saturation = s
                            lightness = l
                        }

                    SaturationLightnessSelectorShape.Diamond ->
                        SelectorDiamondSaturationLightnessHSL(
                            modifier = Modifier.fillMaxWidth(ringInnerRadiusFraction * .9f),
                            hue = hue,
                            saturation = saturation,
                            lightness = lightness,
                            selectionRadius = selectionRadius
                        ) { s, l ->
                            saturation = s
                            lightness = l
                        }
                }
            }
        }

        // HSL-HSV-RGB Color Model Change Tabs
        if (isColorModelSelectable) {
            ColorModelChangeTabRow(
                modifier = Modifier.width(350.dp),
                colorModel = inputColorModel,
                onColorModelChange = { inputColorModel = it }
            )
        }

        // HSL-HSV-RGB Sliders
        CompositeSliderPanel(
            modifier = Modifier.padding(start = 10.dp, end = 7.dp),
            compositeColor = ColorHSL(
                hue = hue,
                saturation = saturation,
                lightness = lightness,
                alpha = alpha
            ),
            onColorChange = {
                (it as? ColorHSL)?.let { color ->
                    hue = color.hue
                    saturation = color.saturation
                    lightness = color.lightness
                    alpha = color.alpha
                }
            },
            showAlphaSlider = showAlphaSlider,
            inputColorModel = inputColorModel,
            outputColorModel = ColorModel.HSL
        )
    }
}
