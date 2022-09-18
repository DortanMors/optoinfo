package com.ssau.optoinformatic.plot

import com.ssau.optoinformatic.math.*
import com.ssau.optoinformatic.plot.Constants.FX_LABEL
import com.ssau.optoinformatic.plot.Constants.F_KSI_LABEL
import com.ssau.optoinformatic.plot.Constants.KSI_LABEL
import com.ssau.optoinformatic.plot.Constants.X_LABEL
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
