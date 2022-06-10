package lab1

import jetbrains.letsPlot.geom.geomLine
import jetbrains.letsPlot.label.ggtitle
import jetbrains.letsPlot.letsPlot
import space.kscience.kmath.complex.Complex
import space.kscience.kmath.complex.ComplexField
import space.kscience.kmath.complex.ComplexField.times
import space.kscience.kmath.complex.r
import space.kscience.kmath.complex.theta
import space.kscience.kmath.operations.sumWith

fun DoubleRange.integrate(f: ((Double) -> Complex)): Complex = map { x -> f(x) * step }.sumWith(ComplexField)
fun ((Double) -> Complex).integralTransform(ksi: Double, range: DoubleRange, kor: (Double, Double) -> Complex) =
    range.integrate { x -> kor(ksi, x) * this.invoke(x) }

fun ((Double) -> Complex).integralTransform(ksiRange: DoubleRange, xRange: DoubleRange, kor: (Double, Double) -> Complex) =
     ksiRange.map { ksi -> integralTransform(ksi, xRange, kor) }

fun ((Double) -> Complex).integral2Transform(ksiRange: DoubleRange, xRange: DoubleRange, kor: (Double, Double) -> Complex) =
    ksiRange.map { ksi -> integralTransform(ksi, xRange, kor) }

fun List<Complex>.args() = map { it.theta }
fun Array<Complex>.args() = map { it.theta }


fun List<Complex>.absolutes() = map { it.r }
fun Array<Complex>.absolutes() = map { it.r }

fun range(left: Double, right: Double, n: Int) = ((right - left) / n).let { h ->
    (0 until n ).map { k ->
        left + k * h
    }
}

fun plot(data: Map<*, *>, title: String, color: String = "dark-green", size: Double = 1.0) =
    letsPlot(data) +
    geomLine(color = color, size = size) {
        data.keys.run {
            x = first()
            y = last()
        }
    } +
    ggtitle(title)

operator fun ((Double, Double) -> Complex).invoke(xRange: DoubleRange, yRange: DoubleRange) =
    Array(xRange.n) { xi ->
        Array(yRange.n) { yi ->
            invoke(xRange[xi], yRange[yi])
        }
    }
