package com.ssau.plot

import com.ssau.math.*
import com.ssau.plot.Constants.EXT
import com.ssau.plot.Constants.F_KSI_LABEL
import com.ssau.plot.Constants.FX_LABEL
import com.ssau.plot.Constants.KSI_LABEL
import com.ssau.plot.Constants.X_LABEL
import jetbrains.datalore.base.values.Color
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomLine
import jetbrains.letsPlot.label.ggtitle
import jetbrains.letsPlot.letsPlot
import space.kscience.kmath.complex.Complex

fun plotTransformToFile(
    a: Double,
    p1: Double,
    q1: Double,
    c1: Double,
    n: Int,
    m: Int,
    f: (Double) -> Complex,
    kor: (Double, Double) -> Complex
) {
    val ksiRange = DoubleRange(p1, q1, m, KSI_LABEL)
    f.integralTransform(ksiRange, DoubleRange(-c1, c1, n, X_LABEL), kor).run {
        plotOnRange(ksiRange, args(), F_KSI_LABEL,"Args of f transform (α = $a, p = $p1, q = $q1)")
        plotOnRange(ksiRange, absolutes(), F_KSI_LABEL,"Absolutes of f transform (α = $a, p = $p1, q = $q1)")
    }
}

fun plotDefaultFToFile(b: Double, c: Double, n: Int, f: (Double) -> Complex) {
    val xRange = DoubleRange(-c, c, n, X_LABEL)
    plotOnRange(xRange, f(xRange).args(), FX_LABEL, "Args of f default (β = $b)")
    plotOnRange(xRange, f(xRange).absolutes(), FX_LABEL, "Absolutes of f default (β = $b)")
}

fun plotOnRange(xData: DoubleRange, yData: List<Double>, yLabel: String, title: String) =
    println(
        ggsave(
            plot(
                xData.to(yData, yLabel),
                title
            ),
            "$title$EXT"
        )
    )

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

val String.png: String
    get() = this + EXT
