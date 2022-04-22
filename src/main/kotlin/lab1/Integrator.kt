package lab1

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

fun List<Complex>.args() = map { it.theta }

fun List<Complex>.absolutes() = map { it.r }

fun range(left: Double, right: Double, n: Int) = ((right - left) / n).let { h ->
    (0 until n ).map { k ->
        left + k * h
    }
}