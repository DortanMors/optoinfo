package lab1

import space.kscience.kmath.complex.Complex
import space.kscience.kmath.complex.ComplexField
import space.kscience.kmath.complex.ComplexField.times
import space.kscience.kmath.operations.sumWith

fun integrate(a: Double, b: Double, n: Int, f: (Double) -> Complex): Complex {
    val h = (b-a) / n
    return (0 until n).map { k ->
        val xk = a + k * h
        val fk = f(xk)
        fk * h
    }.sumWith(ComplexField)
}
