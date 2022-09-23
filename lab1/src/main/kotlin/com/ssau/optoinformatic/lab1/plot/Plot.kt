package com.ssau.optoinformatic.lab1.plot

import com.ssau.optoinformatic.common.math.*
import com.ssau.optoinformatic.common.plot.Constants.FX_LABEL
import com.ssau.optoinformatic.common.plot.Constants.F_KSI_LABEL
import com.ssau.optoinformatic.common.plot.Constants.KSI_LABEL
import com.ssau.optoinformatic.common.plot.Constants.X_LABEL
import com.ssau.optoinformatic.common.plot.plotOnRange
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
        plotOnRange(
            xData = ksiRange,
            yData = args(),
            yLabel = "Arg($F_KSI_LABEL",
            title = "Args of f transform (α = $a, p = $p1, q = $q1)",
        )
        plotOnRange(
            xData = ksiRange,
            yData = absolutes(),
            yLabel = "|$F_KSI_LABEL",
            title = "Absolutes of f transform (α = $a, p = $p1, q = $q1)",
        )
    }
}

fun plotDefaultFToFile(b: Double, c: Double, n: Int, f: (Double) -> Complex) {
    val xRange = DoubleRange(-c, c, n, X_LABEL)
    plotOnRange(
        xData = xRange,
        yData = f(xRange).args(),
        yLabel = "Arg($FX_LABEL)",
        title = "Args of f default (β = $b)",
    )
    plotOnRange(
        xData = xRange,
        yData = f(xRange).absolutes(),
        yLabel = "|$FX_LABEL|",
        title = "Absolutes of f default (β = $b)",
    )
}
