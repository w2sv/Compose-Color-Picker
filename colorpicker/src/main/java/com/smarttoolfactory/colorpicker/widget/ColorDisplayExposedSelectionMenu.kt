package com.smarttoolfactory.colorpicker.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttoolfactory.colorpicker.model.ColorModel
import com.smarttoolfactory.extendedcolors.util.ColorUtil
import com.smarttoolfactory.extendedcolors.util.fractionToPercent
import com.smarttoolfactory.extendedcolors.util.fractionToRGBString
import com.smarttoolfactory.extendedcolors.util.round

/**
 * Selection menu that displays Color's components in RGB, HSL, or HSL
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ColorDisplayExposedSelectionMenu(
    color: Color,
    initialColorModel: ColorModel,
    backgroundColor: Color,
    textColor: Color,
    dropdownMenuItemColors: DropdownMenuItemColors
) {
    var colorModel by remember { mutableStateOf(initialColorModel) }
    var selectedIndex by remember { mutableIntStateOf(colorModel.ordinal) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        ExposedSelectionMenu(
            modifier = Modifier.width(100.dp),
            index = selectedIndex,
            textFieldColors = ExposedDropdownMenuDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = textColor,
                unfocusedLabelColor = textColor,
                trailingIconColor = textColor,
                focusedTrailingIconColor = textColor,
                textColor = textColor
            ),
            dropdownMenuItemColors = dropdownMenuItemColors,
            options = ColorModel.entries.map { it.name },
            onSelected = {
                selectedIndex = it
                colorModel = ColorModel.entries[it]
            }
        )

        ColorComponentsDisplay(
            color = color,
            colorModel = colorModel,
            textColor = textColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
private fun ColorComponentsDisplayPrev() {
    Surface {
        ColorComponentsDisplay(
            color = Color.Red,
            colorModel = ColorModel.RGB,
            textColor = Color.Black
        )
    }
}

private data class ColorComponent(val label: String, val value: String)

@Composable
fun ColorComponentsDisplay(
    color: Color,
    colorModel: ColorModel,
    textColor: Color,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start
) {
    val components = remember(color, colorModel) { color.componentsFor(colorModel) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement,
        modifier = modifier
    ) {
        components.forEach { component ->
            ColorValueColumn(
                label = component.label,
                value = component.value,
                textColor = textColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun Color.componentsFor(colorModel: ColorModel): List<ColorComponent> {
    val alpha = ColorComponent(
        label = "A",
        value = "${alpha.fractionToPercent()}%"
    )

    return when (colorModel) {
        ColorModel.RGB -> listOf(
            ColorComponent("R", red.fractionToRGBString()),
            ColorComponent("G", green.fractionToRGBString()),
            ColorComponent("B", blue.fractionToRGBString()),
            alpha
        )

        ColorModel.HSV -> {
            val hsv = ColorUtil.colorToHSV(this)
            listOf(
                ColorComponent("H", "${hsv[0].round()}°"),
                ColorComponent("S", "${hsv[1].fractionToPercent()}%"),
                ColorComponent("V", "${hsv[2].fractionToPercent()}%"),
                alpha
            )
        }

        ColorModel.HSL -> {
            val hsl = ColorUtil.colorToHSL(this)
            listOf(
                ColorComponent("H", "${hsl[0].round()}°"),
                ColorComponent("S", "${hsl[1].fractionToPercent()}%"),
                ColorComponent("L", "${hsl[2].fractionToPercent()}%"),
                alpha
            )
        }
    }
}

@Composable
private fun ColorValueColumn(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    textColor: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = textColor, fontSize = 14.sp)
    }
}
