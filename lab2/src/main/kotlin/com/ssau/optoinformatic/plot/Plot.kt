package com.ssau.optoinformatic.plot

import com.ssau.optoinformatic.math.DoubleRange
import com.ssau.optoinformatic.math.SampledArea
import com.ssau.optoinformatic.math.absolutes
import com.ssau.optoinformatic.math.args
import jetbrains.datalore.base.values.Color
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.label.ggtitle
import org.jetbrains.letsPlot.letsPlot
import space.kscience.kmath.complex.Complex

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

fun plotHeat(matrix: Array<Array<Complex>>, title: String) {
    println(title)
    println("Absolutes:" + matrix.map { it.absolutes() })
    println("Args" + matrix.map { it.args() })
}

fun plotToFile(area: SampledArea, fLabel: String, title: String) {
    plotOnRange(area.range, area.numbers.toList().args(), "|$fLabel|", "$title args")
    plotOnRange(area.range, area.numbers.toList().absolutes(), "Arg $fLabel", "$title absolutes")
}