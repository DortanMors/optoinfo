package lab1

import jetbrains.letsPlot.export.ggsave
import lab2.invoke
import space.kscience.kmath.complex.ComplexField.exp
import space.kscience.kmath.complex.ComplexField.i
import space.kscience.kmath.complex.ComplexField.times
import kotlin.math.exp
import kotlin.math.pow

var β = 0.1
var α = 1.0
var c = 5.0
var p = -25.0
var q = 25.0
var n = 1000
var m = 1000

const val X_LABEL = "x"
const val Y_LABEL = "y"
const val KSI_LABEL = "ξ"
const val FX_LABEL = "f($X_LABEL)"
const val FKSI_LABEL = "F($KSI_LABEL)"
const val EXT = ".png"

val f = { x: Double -> exp(i * β * x) }

val kor = { ksi: Double, x: Double ->
    i * exp(-α * (x - ksi).pow(2))
} // Fomin: variant 12

fun main() {
    // Task 1
    listOf(0.1, 100.0, 0.001, -0.1, -0.001).forEach(::plotDefaultFToFile)

    // Task 2
    plotTransformToFile(p1 = p, q1 =q)

    // Task 3
    plotTransformToFile(p1 = p * 2, q1 = q * 2)
    plotTransformToFile(p1 = p * 0.5, q1 = q * 0.5)

    // Task 4
    listOf(0.1, 100.0, 0.001).forEach { plotTransformToFile(a = it) }

    // Task 5
    listOf(c, 1.0, 100.0).forEach { plotTransformToFile(c1 = it) }

}

fun plotTransformToFile(a: Double = α, p1: Double = p, q1: Double = q, c1: Double = c) {
    val prevA = α
    α = a
    val ksiRange = DoubleRange(p1, q1, m, KSI_LABEL)
    f.integralTransform(ksiRange, DoubleRange(-c1, c1, n, X_LABEL), kor).run {
        plotOnRange(ksiRange, args(), FKSI_LABEL,"Args of f transform (α = $α, p = $p1, q = $q1)")
        plotOnRange(ksiRange, absolutes(), FKSI_LABEL,"Absolutes of f transform (α = $α, p = $p1, q = $q1)")
    }
    α = prevA
}

fun plotDefaultFToFile(b: Double) {
    val prevB = β
    β = b
    val xRange = DoubleRange(-c, c, n, X_LABEL)
    plotOnRange(xRange, f(xRange).args(), FX_LABEL, "Args of f default (β = $β)")
    plotOnRange(xRange, f(xRange).absolutes(), FX_LABEL, "Absolutes of f default (β = $β)")
    β = prevB
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