package com.ssau.optoinformatic.lab2.plot

import com.ssau.optoinformatic.common.math.DoubleRange
import com.ssau.optoinformatic.lab2.math.SampledArea
import com.ssau.optoinformatic.common.math.absolutes
import com.ssau.optoinformatic.common.math.args
import com.ssau.optoinformatic.common.plot.plotOnRange
import com.ssau.optoinformatic.common.plot.png
import jetbrains.datalore.base.values.Color
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.geom.geomTile
import org.jetbrains.letsPlot.ggplot
import org.jetbrains.letsPlot.label.ggtitle
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.scale.scaleFillGradient2

fun twoPlots(title: String, range: DoubleRange, firstArea: List<Double>, secondArea: List<Double>, firstAreaTitle: String, secondAreaTitle: String) {
    val data = mutableMapOf<String, Any>(
        range.label to range,
        firstAreaTitle to firstArea,
        secondAreaTitle to secondArea
    )
    ggsave(
        letsPlot(data) +
                geomLine(color = Color.GREEN, size = 5.0) {
                    data.keys.run {
                        x = range.label
                        y = firstAreaTitle
                    }
                } +
                geomLine(color = Color.RED, size = 1.0) {
                    data.keys.run {
                        x = range.label
                        y = secondAreaTitle
                    }
                } +
                ggtitle(title),
        title.png
    )
}

fun threePlots(
    range: DoubleRange,
    title: String,
    first: List<Double>,
    firstTitle: String,
    second: List<Double>,
    secondTitle: String,
    third: List<Double>,
    thirdTitle: String
) {
    val data = mutableMapOf<String, Any>(
        range.label to range,
        firstTitle to first,
        secondTitle to second,
        thirdTitle to third
    )
    ggsave(
        letsPlot(data) +
                geomLine(color = Color.GREEN, size = 6.0) {
                    data.keys.run {
                        x = range.label
                        y = firstTitle
                    }
                } +
                geomLine(color = Color.RED, size = 4.0) {
                    data.keys.run {
                        x = range.label
                        y = secondTitle
                    }
                } +
                geomLine(color = Color.BLUE, size = 2.0) {
                    data.keys.run {
                        x = range.label
                        y = thirdTitle
                    }
                } +
                ggtitle(title),
        title.png
    )
}

fun plotHeat(matrix: List<List<Double>>, xRange: DoubleRange, yRange: DoubleRange, zTitle: String, title: String) {
    val zFlatten = matrix.flatten()
    val yFlatten = xRange.flatMap { yRange }
    val xFlatten = xRange.flatMap { x -> Array(yRange.size) { x }.asList() }

    ggsave(
        plot = ggplot(mapOf(xRange.label to xFlatten, yRange.label to yFlatten, zTitle to zFlatten))
               + geomTile { x = xRange.label; y = yRange.label; fill=zTitle }
               + scaleFillGradient2(low = "green", mid = "blue", high = "red")
        ,
        filename = title.png
    )
    println(title)
}

fun plotToFile(area: SampledArea, fLabel: String, title: String) {
    plotOnRange(area.range, area.numbers.toList().args(), "Arg $fLabel", "$title args")
    plotOnRange(area.range, area.numbers.toList().absolutes(), "|$fLabel|", "$title absolutes")
}