package lab2

import lab1.*
import space.kscience.kmath.complex.Complex
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow

val inputF: ((Double) -> Complex) = { x -> Complex(exp(-abs(x))) } // Variant 12
val gaussF: ((Double) -> Complex) = { x -> Complex(exp(-x.pow(2))) }

fun main() {
// Task 2
    var n = 100
    var c = 5.0
    val xRange = DoubleRange(-c, c, n, X_LABEL)
    val args = xRange.asArray()
    val area = SampledArea(gaussF.invoke(args), xRange, xRange.step)
    plotToFile(area, "exp(-(x^2))", "gauss beam")
// Task 3
    var m = 1024
    val fourierArea = area.scaledFiniteFourierTransform(m)
    plotToFile(area, "F[exp(-(x^2))]", "fourier gauss beam")
// Task 4
}

fun plotToFile(area: SampledArea, fLabel: String, title: String) {
    plotOnRange(area.range, area.numbers.toList().args(), fLabel, title)
    plotOnRange(area.range, area.numbers.toList().absolutes(), fLabel, title)
}

fun DoubleRange.asArray() = Array(size) { index ->
    left + index * step
}